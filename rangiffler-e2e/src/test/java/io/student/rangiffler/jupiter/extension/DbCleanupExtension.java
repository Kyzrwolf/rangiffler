package io.student.rangiffler.jupiter.extension;

import io.student.rangiffler.service.UserDbClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.ExtensionContext;

@Slf4j
public class DbCleanupExtension implements SuiteExtension {

    private final UserDbClient usersDb = new UserDbClient();

    @Override
    public void beforeSuite(ExtensionContext context) {
        log.info("Очистка пользователей в базах auth и api перед запуском тестов");
        usersDb.cleanUsers();
    }
}