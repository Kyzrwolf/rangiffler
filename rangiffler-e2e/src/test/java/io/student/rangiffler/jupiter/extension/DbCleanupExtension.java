package io.student.rangiffler.jupiter.extension;

import io.student.rangiffler.service.UserDbClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import javax.annotation.Nonnull;

@Slf4j
public class DbCleanupExtension implements BeforeAllCallback {

    private static final ExtensionContext.Namespace NAMESPACE =
            ExtensionContext.Namespace.create(DbCleanupExtension.class);

    private static final String CLEANED_KEY = "db_cleaned";

    private final UserDbClient usersDb = new UserDbClient();

    @Override
    public void beforeAll(@Nonnull ExtensionContext context) {
        context.getRoot()
                .getStore(NAMESPACE)
                .getOrComputeIfAbsent(CLEANED_KEY, key -> {
                    log.info("Очистка пользователей в базах auth и api перед запуском тестов");
                    usersDb.cleanUsers();
                    return Boolean.TRUE;
                });
    }
}