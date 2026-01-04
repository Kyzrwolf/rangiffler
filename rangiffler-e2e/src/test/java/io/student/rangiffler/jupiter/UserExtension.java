package io.student.rangiffler.jupiter;

import io.student.rangiffler.models.UserJson;
import io.student.rangiffler.service.UserDbClient;
import io.student.rangiffler.service.UsersClient;
import net.datafaker.Faker;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

public class UserExtension implements BeforeEachCallback, ParameterResolver {
    private static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UserExtension.class);
    private final UsersClient usersClient = new UserDbClient();
    private final Faker faker = new Faker();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(annotation -> {
                    context.getStore(NAMESPACE)
                            .put(context.getUniqueId(), usersClient.createUser(faker.credentials().username(), faker.credentials().password(3, 12)));
                });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(UserJson.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), UserJson.class);
    }
}
