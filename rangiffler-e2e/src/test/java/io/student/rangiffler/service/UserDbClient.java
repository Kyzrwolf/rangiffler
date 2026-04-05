package io.student.rangiffler.service;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.mysql.cj.jdbc.MysqlXADataSource;
import io.student.rangiffler.config.Config;
import io.student.rangiffler.data.auth.AuthUserEntity;
import io.student.rangiffler.data.auth.AuthorityEntity;
import io.student.rangiffler.data.userdata.UdUserEntity;
import io.student.rangiffler.models.UserJson;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.atomikos.icatch.jta.UserTransactionManager;
import java.sql.Connection;
import java.util.UUID;

public class UserDbClient implements UsersClient {

    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder PASSWORD_ENCODER =
            PasswordEncoderFactories.createDelegatingPasswordEncoder();

    // sql auth schema
    private static final String INSERT_AUTH_USER_SQL =
            "INSERT INTO `user` (id, username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) " +
            "VALUES (UUID_TO_BIN(?, true), ?, ?, true, true, true, true)";

    private static final String INSERT_AUTHORITY_SQL =
            "INSERT INTO `authority` (user_id, authority) VALUES (UUID_TO_BIN(?, true), ?)";

    private static final String SELECT_AUTH_USER_SQL =
            "SELECT BIN_TO_UUID(u.id, true) AS id, u.username, u.password, u.enabled, " +
            "u.account_non_expired, u.account_non_locked, u.credentials_non_expired, " +
            "a.authority " +
            "FROM `user` u " +
            "LEFT JOIN `authority` a ON a.user_id = u.id " +
            "WHERE u.username = ?";

    private static final String DELETE_AUTHORITY_SQL =
            "DELETE FROM `authority` WHERE user_id = UUID_TO_BIN(?, true)";

    private static final String DELETE_AUTH_USER_SQL =
            "DELETE FROM `user` WHERE id = UUID_TO_BIN(?, true)";

    // sql userdata schema
    private static final String SELECT_DEFAULT_COUNTRY_SQL =
            "SELECT BIN_TO_UUID(id, true) AS id FROM `country` WHERE code = 'ru' LIMIT 1";

    private static final String INSERT_USERDATA_USER_SQL =
            "INSERT INTO `user` (id, username, country_id) " +
            "VALUES (UUID_TO_BIN(?, true), ?, UUID_TO_BIN(?, true))";

    private static final String SELECT_USERDATA_USER_SQL =
            "SELECT BIN_TO_UUID(u.id, true) AS id, u.username, u.firstname, u.lastName " +
            "FROM `user` u WHERE u.username = ?";

    private static final String INSERT_FRIENDSHIP_SQL =
            "INSERT INTO `friendship` (requester_id, addressee_id, created_date, status) " +
            "VALUES (UUID_TO_BIN(?, true), UUID_TO_BIN(?, true), NOW(), ?)";

    private static final String DELETE_FRIENDSHIP_SQL =
            "DELETE FROM `friendship` " +
            "WHERE requester_id = UUID_TO_BIN(?, true) OR addressee_id = UUID_TO_BIN(?, true)";

    private static final String DELETE_USERDATA_USER_SQL =
            "DELETE FROM `user` WHERE id = UUID_TO_BIN(?, true)";

    private static final UserTransactionManager TRANSACTION_MANAGER;
    private static final AtomikosDataSourceBean AUTH_DS;
    private static final AtomikosDataSourceBean USERDATA_DS;
    private static final UUID DEFAULT_COUNTRY_ID;

    static {
        try {
            System.setProperty("com.atomikos.icatch.log_base_dir",
                    System.getProperty("java.io.tmpdir") + "/atomikos-rangiffler");
            System.setProperty("com.atomikos.icatch.tm_unique_name", "rangiffler-e2e-tm");
            System.setProperty("com.atomikos.icatch.enable_logging", "false");
            System.setProperty("com.atomikos.icatch.force_shutdown_on_vm_exit", "true");

            TRANSACTION_MANAGER = new UserTransactionManager();
            TRANSACTION_MANAGER.setForceShutdown(true);
            TRANSACTION_MANAGER.init();

            AUTH_DS = buildAtomikosDs("rangiffler-auth", CFG.authJdbcUrl());
            USERDATA_DS = buildAtomikosDs("rangiffler-userdata", CFG.userdataJdbcUrl());

            DEFAULT_COUNTRY_ID = loadDefaultCountryId();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                AUTH_DS.close();
                USERDATA_DS.close();
                TRANSACTION_MANAGER.close();
            }));
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize UserDbClient XA infrastructure", e);
        }
    }

    private static final ResultSetExtractor<AuthUserEntity> AUTH_USER_EXTRACTOR = rs -> {
        AuthUserEntity user = null;
        while (rs.next()) {
            if (user == null) {
                user = new AuthUserEntity();
                user.setId(UUID.fromString(rs.getString("id")));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setEnabled(rs.getBoolean("enabled"));
                user.setAccountNonExpired(rs.getBoolean("account_non_expired"));
                user.setAccountNonLocked(rs.getBoolean("account_non_locked"));
                user.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
            }
            String authority = rs.getString("authority");
            if (authority != null) {
                AuthorityEntity ae = new AuthorityEntity();
                ae.setAuthority(authority);
                user.getAuthorities().add(ae);
            }
        }
        return user;
    };

    @Override
    public UserJson createUser(String username, String password) {
        UUID authUserId = UUID.randomUUID();
        UUID udUserId = UUID.randomUUID();
        String encodedPassword = PASSWORD_ENCODER.encode(password);

        // mysql does not support xa RESUME, so we must hold connections open for the entire transaction
        // We get raw XA connections once (after begin()), wrap them in
        // SingleConnectionDataSource(suppressClose=true) so JdbcTemplate cannot release them
        // between calls, then release them manually before commit.
        Connection rawAuthConn = null;
        Connection rawUdConn = null;
        try {
            TRANSACTION_MANAGER.begin();

            rawAuthConn = AUTH_DS.getConnection();
            rawUdConn = USERDATA_DS.getConnection();

            JdbcTemplate authJdbc = jdbcTemplate(rawAuthConn);
            authJdbc.update(INSERT_AUTH_USER_SQL, authUserId.toString(), username, encodedPassword);
            authJdbc.update(INSERT_AUTHORITY_SQL, authUserId.toString(), "read");
            authJdbc.update(INSERT_AUTHORITY_SQL, authUserId.toString(), "write");

            jdbcTemplate(rawUdConn).update(INSERT_USERDATA_USER_SQL,
                    udUserId.toString(), username, DEFAULT_COUNTRY_ID.toString());

            rawAuthConn.close();
            rawAuthConn = null;
            rawUdConn.close();
            rawUdConn = null;

            TRANSACTION_MANAGER.commit();
        } catch (Exception e) {
            closeQuietly(rawAuthConn);
            closeQuietly(rawUdConn);
            rollbackQuietly();
            throw new RuntimeException("Failed to create user [" + username + "]: " + e.getMessage(), e);
        }

        return new UserJson(authUserId, udUserId, username, password, null, null, null);
    }

    @Override
    public UserJson findByUsername(String username) {
        AuthUserEntity authUser = new JdbcTemplate(authReadDataSource())
                .query(SELECT_AUTH_USER_SQL, AUTH_USER_EXTRACTOR, username);

        if (authUser == null) {
            throw new RuntimeException("Auth user not found: " + username);
        }

        UdUserEntity udUser = new JdbcTemplate(userdataReadDataSource())
                .queryForObject(SELECT_USERDATA_USER_SQL,
                        (rs, rowNum) -> {
                            UdUserEntity u = new UdUserEntity();
                            u.setId(UUID.fromString(rs.getString("id")));
                            u.setUsername(rs.getString("username"));
                            u.setFirstname(rs.getString("firstname"));
                            u.setLastName(rs.getString("lastName"));
                            return u;
                        }, username);

        return new UserJson(
                authUser.getId(),
                udUser != null ? udUser.getId() : null,
                username,
                authUser.getPassword(),
                udUser != null ? udUser.getFirstname() : null,
                udUser != null ? udUser.getLastName() : null,
                null
        );
    }

    @Override
    public void addFriendship(UserJson requester, UserJson addressee) {
        JdbcTemplate jdbc = new JdbcTemplate(userdataReadDataSource());
        jdbc.update(INSERT_FRIENDSHIP_SQL,
                requester.udId().toString(), addressee.udId().toString(), "ACCEPTED");
        jdbc.update(INSERT_FRIENDSHIP_SQL,
                addressee.udId().toString(), requester.udId().toString(), "ACCEPTED");
    }

    @Override
    public void addPendingRequest(UserJson requester, UserJson addressee) {
        new JdbcTemplate(userdataReadDataSource())
                .update(INSERT_FRIENDSHIP_SQL,
                        requester.udId().toString(), addressee.udId().toString(), "PENDING");
    }

    @Override
    public void deleteUser(UserJson user) {
        Connection rawAuthConn = null;
        Connection rawUdConn = null;
        try {
            TRANSACTION_MANAGER.begin();

            rawAuthConn = AUTH_DS.getConnection();
            rawUdConn = USERDATA_DS.getConnection();

            JdbcTemplate udJdbc = jdbcTemplate(rawUdConn);
            udJdbc.update(DELETE_FRIENDSHIP_SQL, user.udId().toString(), user.udId().toString());
            udJdbc.update(DELETE_USERDATA_USER_SQL, user.udId().toString());

            JdbcTemplate authJdbc = jdbcTemplate(rawAuthConn);
            authJdbc.update(DELETE_AUTHORITY_SQL, user.id().toString());
            authJdbc.update(DELETE_AUTH_USER_SQL, user.id().toString());

            rawAuthConn.close();
            rawAuthConn = null;
            rawUdConn.close();
            rawUdConn = null;

            TRANSACTION_MANAGER.commit();
        } catch (Exception e) {
            closeQuietly(rawAuthConn);
            closeQuietly(rawUdConn);
            rollbackQuietly();
            throw new RuntimeException("Failed to delete user [" + user.username() + "]: " + e.getMessage(), e);
        }
    }

    // helpers
    private static JdbcTemplate jdbcTemplate(Connection conn) {
        return new JdbcTemplate(new SingleConnectionDataSource(conn, true));
    }

    private static AtomikosDataSourceBean buildAtomikosDs(String name, String jdbcUrl) throws Exception {
        MysqlXADataSource xaDs = new MysqlXADataSource();
        xaDs.setUrl(jdbcUrl);
        xaDs.setUser(CFG.dbUsername());
        xaDs.setPassword(CFG.dbPassword());

        AtomikosDataSourceBean ds = new AtomikosDataSourceBean();
        ds.setUniqueResourceName(name);
        ds.setXaDataSource(xaDs);
        ds.setMaxPoolSize(5);
        ds.init();
        return ds;
    }

    private static UUID loadDefaultCountryId() {
        String idStr = new JdbcTemplate(
                new DriverManagerDataSource(CFG.userdataJdbcUrl(), CFG.dbUsername(), CFG.dbPassword()))
                .queryForObject(SELECT_DEFAULT_COUNTRY_SQL, String.class);
        if (idStr == null) {
            throw new RuntimeException("Default country 'ru' not found in rangiffler-api schema");
        }
        return UUID.fromString(idStr);
    }

    private static DriverManagerDataSource authReadDataSource() {
        return new DriverManagerDataSource(CFG.authJdbcUrl(), CFG.dbUsername(), CFG.dbPassword());
    }

    private static DriverManagerDataSource userdataReadDataSource() {
        return new DriverManagerDataSource(CFG.userdataJdbcUrl(), CFG.dbUsername(), CFG.dbPassword());
    }

    private static void closeQuietly(Connection conn) {
        if (conn != null) {
            try { conn.close(); } catch (Exception ignored) {}
        }
    }

    private static void rollbackQuietly() {
        try { TRANSACTION_MANAGER.rollback(); } catch (Exception ignored) {}
    }
}