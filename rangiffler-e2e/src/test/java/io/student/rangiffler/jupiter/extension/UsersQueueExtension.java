package io.student.rangiffler.jupiter.extension;

import io.student.rangiffler.jupiter.annotation.UserType;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class UsersQueueExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UsersQueueExtension.class);

    public record StaticUser(String username, String password, UserType.Type type) {
    }

    private static Queue<StaticUser> EMPTY_USERS = new ConcurrentLinkedQueue<>();
    private static Queue<StaticUser> WITH_FRIENDS = new ConcurrentLinkedQueue<>();
    private static Queue<StaticUser> WITH_INCOME_REQUESTS = new ConcurrentLinkedQueue<>();
    private static Queue<StaticUser> WITH_OUTCOME_REQUESTS = new ConcurrentLinkedQueue<>();

    static {
        EMPTY_USERS.add(new StaticUser("empty", "123", UserType.Type.EMPTY));
        WITH_FRIENDS.add(new StaticUser("Bobert", "123", UserType.Type.WITH_FRIEND));
        WITH_INCOME_REQUESTS.add(new StaticUser("Sofia", "123", UserType.Type.WITH_INCOME_REQUEST));
        WITH_OUTCOME_REQUESTS.add(new StaticUser("Gandalf", "123", UserType.Type.WITH_OUTCOME_REQUEST));
    }

    @Override
    public void beforeEach(ExtensionContext context) {

        ExtensionContext.Store store = context.getStore(NAMESPACE);
        Map<UserType.Type, StaticUser> users =
                (Map<UserType.Type, StaticUser>) store.getOrComputeIfAbsent(
                        context.getUniqueId(),
                        key -> new HashMap<UserType.Type, StaticUser>()
                );

        Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(p -> AnnotationSupport.isAnnotated(p, UserType.class))
                .forEach(parameter -> {
                    UserType ut = parameter.getAnnotation(UserType.class);
                    if (users.containsKey(ut.value())) {
                        throw new ExtensionConfigurationException(
                                "Duplicate @UserType(" + ut.value() + ") in test method: "
                                        + context.getDisplayName()
                        );
                    }
                    StaticUser user = pollUser(ut.value());
                    users.put(ut.value(), user);
                });
    }

    private StaticUser pollUser(UserType.Type type) {
        StopWatch sw = StopWatch.createStarted();
        StaticUser user = null;

        while (user == null && sw.getTime(TimeUnit.SECONDS) < 30) {
            user = switch (type) {
                case EMPTY -> EMPTY_USERS.poll();
                case WITH_FRIEND -> WITH_FRIENDS.poll();
                case WITH_INCOME_REQUEST -> WITH_INCOME_REQUESTS.poll();
                case WITH_OUTCOME_REQUEST -> WITH_OUTCOME_REQUESTS.poll();
            };
        }

        if (user == null) {
            throw new IllegalStateException("no users found after 30 seconds");
        }
        return user;
    }


    @Override
    public void afterEach(ExtensionContext context) {
        Map<UserType.Type, StaticUser> users =
                context.getStore(NAMESPACE)
                        .remove(context.getUniqueId(), Map.class);

        users.values().forEach(user -> {
            switch (user.type()) {
                case EMPTY -> EMPTY_USERS.add(user);
                case WITH_FRIEND -> WITH_FRIENDS.add(user);
                case WITH_INCOME_REQUEST -> WITH_INCOME_REQUESTS.add(user);
                case WITH_OUTCOME_REQUEST -> WITH_OUTCOME_REQUESTS.add(user);
            }
        });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(StaticUser.class)
                && AnnotationSupport.isAnnotated(parameterContext.getParameter(), UserType.class);
    }

    @Override
    public StaticUser resolveParameter(ParameterContext pc, ExtensionContext context) {
        UserType ut = pc.getParameter().getAnnotation(UserType.class);
        Map<UserType.Type, StaticUser> users = context.getStore(NAMESPACE)
                .get(context.getUniqueId(), Map.class);

        return users.get(ut.value());
    }
}
