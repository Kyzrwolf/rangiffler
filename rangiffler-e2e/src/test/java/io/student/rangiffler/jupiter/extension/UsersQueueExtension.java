package io.student.rangiffler.jupiter.extension;

import io.student.rangiffler.jupiter.annotation.UserType;
import io.student.rangiffler.models.UserJson;
import io.student.rangiffler.service.UserDbClient;
import io.student.rangiffler.service.UsersClient;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class UsersQueueExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE =
            ExtensionContext.Namespace.create(UsersQueueExtension.class);

    private static final String USERS_KEY = "static_users_";
    private static final String CLEANUP_KEY = "cleanup_users_";

    public record StaticUser(
            String username,
            String password,
            UserType.Type type,
            @Nullable StaticUser friend
    ) {
    }

    private final UsersClient usersClient = new UserDbClient();
    private final Faker faker = new Faker();

    @Override
    public void beforeEach(ExtensionContext context) {
        var store = context.getStore(NAMESPACE);
        var usersMap = new HashMap<UserType.Type, StaticUser>();
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
                    var staticUser = createUserForType(ut.value(), cleanupList);
                    usersMap.put(ut.value(), staticUser);
                });

        store.put(USERS_KEY + context.getUniqueId(), usersMap);
        store.put(CLEANUP_KEY + context.getUniqueId(), cleanupList);
    }

    @Override
    public void afterEach(ExtensionContext context) {
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
    public boolean supportsParameter(ParameterContext parameterContext,
                                     ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(StaticUser.class)
                && AnnotationSupport.isAnnotated(parameterContext.getParameter(), UserType.class);
    }

    @Override
    public StaticUser resolveParameter(ParameterContext pc,
                                       ExtensionContext context) throws ParameterResolutionException {
        UserType ut = pc.getParameter().getAnnotation(UserType.class);
        @SuppressWarnings("unchecked")
        Map<UserType.Type, StaticUser> users = context.getStore(NAMESPACE)
                .get(USERS_KEY + context.getUniqueId(), Map.class);
        return users.get(ut.value());
    }

    private StaticUser createUserForType(UserType.Type type, List<UserJson> cleanupList) {
        String username = faker.credentials().username();
        String password = faker.credentials().password();
        UserJson mainUser = usersClient.createUser(username, password);
        cleanupList.add(mainUser);

        return switch (type) {
            case EMPTY -> new StaticUser(username, password, type, null);

            case WITH_FRIEND -> {
                String friendUsername = faker.credentials().username();
                String friendPassword = faker.credentials().password();
                UserJson friendUser = usersClient.createUser(friendUsername, friendPassword);
                cleanupList.add(friendUser);
                usersClient.addFriendship(mainUser, friendUser);
                yield new StaticUser(username, password, type,
                        new StaticUser(friendUsername, friendPassword, UserType.Type.EMPTY, null));
            }

            case WITH_INCOME_REQUEST -> {
                String requesterUsername = faker.credentials().username();
                String requesterPassword = faker.credentials().password();
                UserJson requester = usersClient.createUser(requesterUsername, requesterPassword);
                cleanupList.add(requester);
                usersClient.addPendingRequest(requester, mainUser);
                yield new StaticUser(username, password, type,
                        new StaticUser(requesterUsername, requesterPassword, UserType.Type.EMPTY, null));
            }

            case WITH_OUTCOME_REQUEST -> {
                String addresseeUsername = faker.credentials().username();
                String addresseePassword = faker.credentials().password();
                UserJson addressee = usersClient.createUser(addresseeUsername, addresseePassword);
                cleanupList.add(addressee);
                usersClient.addPendingRequest(mainUser, addressee);
                yield new StaticUser(username, password, type,
                        new StaticUser(addresseeUsername, addresseePassword, UserType.Type.EMPTY, null));
            }
        };
    }
}
