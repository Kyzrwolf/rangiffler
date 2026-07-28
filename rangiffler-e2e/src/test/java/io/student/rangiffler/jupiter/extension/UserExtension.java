package io.student.rangiffler.jupiter.extension;

import io.student.rangiffler.jupiter.annotation.UserType;
import io.student.rangiffler.model.UserJson;
import io.student.rangiffler.service.UserDbClient;
import io.student.rangiffler.service.UsersClient;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class UserExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE =
            ExtensionContext.Namespace.create(UserExtension.class);

    private static final String USERS_KEY = "static_users_";
    private static final String CLEANUP_KEY = "cleanup_users_";

    public record TestUser(
            @Nonnull String username,
            @Nonnull String password,
            @Nonnull UserType.Type type,
            @Nullable TestUser friend
    ) {
    }

    private final UsersClient usersClient = new UserDbClient();
    private final Faker faker = new Faker();

    @Override
    public void beforeEach(@Nonnull ExtensionContext context) {
        var store = context.getStore(NAMESPACE);
        var usersMap = new HashMap<UserType.Type, TestUser>();
        var cleanupList = new ArrayList<UserJson>();

        Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(p -> AnnotationSupport.isAnnotated(p, UserType.class))
                .map(p -> p.getAnnotation(UserType.class))
                .forEach(ut -> {
                    if (usersMap.containsKey(ut.value())) {
                        throw new ExtensionConfigurationException(
                                "Duplicate @UserType(" + ut.value() + ") in test method: "
                                        + context.getDisplayName()
                        );
                    }
                    var testUser = createUserForType(ut.value(), cleanupList);
                    usersMap.put(ut.value(), testUser);
                });

        store.put(USERS_KEY + context.getUniqueId(), usersMap);
        store.put(CLEANUP_KEY + context.getUniqueId(), cleanupList);
    }

    @Override
    public void afterEach(@Nonnull ExtensionContext context) {
        var store = context.getStore(NAMESPACE);

        @SuppressWarnings("unchecked")
        List<UserJson> cleanupList = store.remove(
                CLEANUP_KEY + context.getUniqueId(), List.class);
        store.remove(USERS_KEY + context.getUniqueId());

        if (cleanupList != null) {
            cleanupList.forEach(user -> {
                try {
                    usersClient.deleteUser(user);
                } catch (Exception e) {
                    log.error("Failed to delete user during cleanup: {}", user.username());
                }
            });
        }
    }

    @Override
    public boolean supportsParameter(@Nonnull ParameterContext parameterContext,
                                     @Nonnull ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(TestUser.class)
                && AnnotationSupport.isAnnotated(parameterContext.getParameter(), UserType.class);
    }

    @Override
    @Nonnull
    public TestUser resolveParameter(@Nonnull ParameterContext pc,
                                     @Nonnull ExtensionContext context) throws ParameterResolutionException {
        UserType ut = pc.getParameter().getAnnotation(UserType.class);
        @SuppressWarnings("unchecked")
        Map<UserType.Type, TestUser> users = context.getStore(NAMESPACE)
                .get(USERS_KEY + context.getUniqueId(), Map.class);
        return users.get(ut.value());
    }

    @Nonnull
    private TestUser createUserForType(@Nonnull UserType.Type type, @Nonnull List<UserJson> cleanupList) {
        String username = faker.credentials().username();
        String password = faker.credentials().password();
        UserJson mainUser = usersClient.createUser(username, password);
        cleanupList.add(mainUser);

        return switch (type) {
            case EMPTY -> new TestUser(username, password, type, null);

            case WITH_FRIEND -> {
                String friendUsername = faker.credentials().username();
                String friendPassword = faker.credentials().password();
                UserJson friendUser = usersClient.createUser(friendUsername, friendPassword);
                cleanupList.add(friendUser);
                usersClient.addFriendship(mainUser, friendUser);
                yield new TestUser(username, password, type,
                        new TestUser(friendUsername, friendPassword, UserType.Type.WITH_FRIEND, null));
            }

            case WITH_INCOME_REQUEST -> {
                String requesterUsername = faker.credentials().username();
                String requesterPassword = faker.credentials().password();
                UserJson requester = usersClient.createUser(requesterUsername, requesterPassword);
                cleanupList.add(requester);
                usersClient.addPendingRequest(requester, mainUser);
                yield new TestUser(username, password, type,
                        new TestUser(requesterUsername, requesterPassword, UserType.Type.WITH_OUTCOME_REQUEST, null));
            }

            case WITH_OUTCOME_REQUEST -> {
                String addresseeUsername = faker.credentials().username();
                String addresseePassword = faker.credentials().password();
                UserJson addressee = usersClient.createUser(addresseeUsername, addresseePassword);
                cleanupList.add(addressee);
                usersClient.addPendingRequest(mainUser, addressee);
                yield new TestUser(username, password, type,
                        new TestUser(addresseeUsername, addresseePassword, UserType.Type.WITH_INCOME_REQUEST, null));
            }

            case STRANGER -> {
                String strangerUsername = faker.credentials().username();
                String strangerPassword = faker.credentials().password();
                UserJson stranger = usersClient.createUser(strangerUsername, strangerPassword);
                cleanupList.add(stranger);
                yield new TestUser(username, password, type,
                        new TestUser(strangerUsername, strangerPassword, UserType.Type.EMPTY, null));
            }
        };
    }
}
