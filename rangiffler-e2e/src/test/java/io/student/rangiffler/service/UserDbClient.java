package io.student.rangiffler.service;

import io.student.rangiffler.config.Config;
import io.student.rangiffler.models.UserJson;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

public class UserDbClient implements UsersClient {

    private static final Config CFG = Config.getInstance();
    private static final String INSERT_USER_SQL = "INSERT INTO users (id, username, password) VALUES (?, ?, ?)";
    private static final String INSERT_AUTHORITY_SQL = "INSERT INTO authority (user_id, authority) VALUES (UUID_TO_BIN(?, true), ?)";
    private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    @Override
    public UserJson createUser(String username, String password) {
        try {
            final var jdbcTemplate = new JdbcTemplate(
                    new SingleConnectionDataSource(CFG.jdbcUrl(), CFG.dbUsername(), CFG.dbPassword(), true));
            final var userId = UUID.randomUUID();
            final var encodedPassword = passwordEncoder.encode(password);

            jdbcTemplate.update(INSERT_USER_SQL,
                    userId, username, encodedPassword);
            jdbcTemplate.update(INSERT_AUTHORITY_SQL,
                    userId, "read");
            jdbcTemplate.update(INSERT_AUTHORITY_SQL,
                    userId, "write");

            return new UserJson(userId, username, encodedPassword, null, null);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create user", e);
        }
    }
}
