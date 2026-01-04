package io.student.rangiffler.service;

import io.student.rangiffler.config.Config;
import io.student.rangiffler.models.UserJson;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

public class UserDbClient implements UsersClient {

    private static final Config CFG = Config.getInstance();
    private static final String INSERT_USER_SQL = """
            INSERT INTO `user` (id, username, password, enabled, account_non_expired,
                                account_non_locked, credentials_non_expired)
            VALUES (UUID_TO_BIN(?, true), ?, ?, true, true, true, true)
            """;
    private static final String INSERT_AUTHORITY_SQL = "INSERT INTO authority (user_id, authority) VALUES (UUID_TO_BIN(?, true), ?)";
    private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Override
    public UserJson createUser(String username, String password) {
        try {
            var jdbcTemplate = getJdbcTemplate();
            final var userId = UUID.randomUUID();

            jdbcTemplate.update(conn -> {
                        var ps = conn.prepareStatement(INSERT_USER_SQL);
                        ps.setString(1, userId.toString());
                        ps.setString(2, username);
                        ps.setString(3, passwordEncoder.encode(password));
                        return ps;
                    }
            );

            jdbcTemplate.update(INSERT_AUTHORITY_SQL,
                    userId.toString(), "read");
            jdbcTemplate.update(INSERT_AUTHORITY_SQL,
                    userId.toString(), "write");


            return new UserJson(userId, username, password, null, null, null);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create user", e);
        }
    }


    private JdbcTemplate getJdbcTemplate() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(CFG.jdbcUrl());
        dataSource.setUsername(CFG.dbUsername());
        dataSource.setPassword(CFG.dbPassword());

        return new JdbcTemplate(dataSource);
    }
}
