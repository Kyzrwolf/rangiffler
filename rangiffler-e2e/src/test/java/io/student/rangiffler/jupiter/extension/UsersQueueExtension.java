package io.student.rangiffler.jupiter.extension;

import io.qameta.allure.Allure;
import io.student.rangiffler.jupiter.annotation.UserType;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
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
        final var user = new Optional[]{Optional.empty()};

        Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(parameter -> AnnotationSupport.isAnnotated(parameter, UserType.class))
                .findFirst()
                .map(parameter -> parameter.getAnnotation(UserType.class))
                .ifPresent(
                        userType -> {
                            StopWatch sw = StopWatch.createStarted();
                            while (user[0].isEmpty() && sw.getTime(TimeUnit.SECONDS) < 30) {
                                switch (userType.value()) {
                                    case EMPTY -> user[0] = Optional.ofNullable(EMPTY_USERS.poll());
                                    case WITH_FRIEND -> user[0] = Optional.ofNullable(WITH_FRIENDS.poll());
                                    case WITH_INCOME_REQUEST ->
                                            user[0] = Optional.ofNullable(WITH_INCOME_REQUESTS.poll());
                                    case WITH_OUTCOME_REQUEST ->
                                            user[0] = Optional.ofNullable(WITH_OUTCOME_REQUESTS.poll());
                                    default -> user[0] = Optional.empty();
                                }
                            }
                            Allure.getLifecycle().updateTestCase(testCase -> testCase.setStart(new Date().getTime()));
                            user[0].ifPresentOrElse(u -> context.getStore(NAMESPACE)
                                            .put(context.getUniqueId(), u),
                                    () -> new IllegalStateException("no users found after 30 seconds"));
                        });
    }


    @Override
    public void afterEach(ExtensionContext context) {
        var staticUser = context.getStore(NAMESPACE).get(context.getUniqueId(), StaticUser.class);
        switch (staticUser.type()) {
            case EMPTY -> EMPTY_USERS.add(staticUser);
            case WITH_FRIEND -> WITH_FRIENDS.add(staticUser);
            case WITH_INCOME_REQUEST -> WITH_INCOME_REQUESTS.add(staticUser);
            case WITH_OUTCOME_REQUEST -> WITH_OUTCOME_REQUESTS.add(staticUser);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(StaticUser.class)
                && AnnotationSupport.isAnnotated(parameterContext.getParameter(), UserType.class);
    }

    @Override
    public StaticUser resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), StaticUser.class);
    }
}
