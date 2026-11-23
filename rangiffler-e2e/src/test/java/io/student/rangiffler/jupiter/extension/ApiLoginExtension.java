package io.student.rangiffler.jupiter.extension;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import io.student.rangiffler.api.AuthApiClient;
import io.student.rangiffler.api.core.ThreadSafeCookieStore;
import io.student.rangiffler.config.Config;
import io.student.rangiffler.jupiter.annotation.ApiLogin;
import io.student.rangiffler.jupiter.annotation.Token;
import io.student.rangiffler.jupiter.annotation.UserType;
import io.student.rangiffler.page.TravelsMapPage;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;
import org.openqa.selenium.Cookie;

import javax.annotation.Nullable;
import java.util.Arrays;

public class ApiLoginExtension implements BeforeEachCallback, ParameterResolver {

    public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(ApiLoginExtension.class);
    private static final Config CFG = Config.getInstance();
    private final AuthApiClient authApiClient = new AuthApiClient();

    @Override
    public void beforeEach(ExtensionContext context) {
       AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), ApiLogin.class)
                .ifPresent(apiLogin -> {
                    var username = apiLogin.username();
                    var password = apiLogin.password();

                    if (username.isEmpty() || password.isEmpty()) {
                        var userType = Arrays.stream(context.getRequiredTestMethod().getParameters())
                                .filter(p -> AnnotationSupport.isAnnotated(p, UserType.class))
                                .findFirst()
                                .map(p -> p.getAnnotation(UserType.class))
                                .orElseThrow(() -> new ExtensionConfigurationException(
                                        "@ApiLogin has no username/password and no @UserType parameter found on test method: "
                                                + context.getDisplayName()
                                ));
                        var testUser = UserExtension.getUser(userType.value(), context);
                        username = testUser.username();
                        password = testUser.password();
                    }

                   var token = authApiClient.login(username, password);
                   setToken(token);

                    if (apiLogin.setupBrowser()) {
                        Selenide.open(CFG.frontUrl());
                        Selenide.localStorage().setItem("id_token", getToken());
                        WebDriverRunner.getWebDriver().manage().addCookie(
                                new Cookie(
                                        "JSESSIONID",
                                        ThreadSafeCookieStore.INSTANCE.cookieValue("JSESSIONID")
                                )
                        );
                        Selenide.open(CFG.frontUrl(), TravelsMapPage.class).checkTravelPageIsOpen();
                    }
                });
    }

        @Override
        public boolean supportsParameter (ParameterContext parameterContext, ExtensionContext extensionContext) throws
        ParameterResolutionException {
            return parameterContext.getParameter().getType().isAssignableFrom(String.class)
                    && AnnotationSupport.isAnnotated(parameterContext.getParameter(), Token.class);
        }

        @Override
        public String resolveParameter (ParameterContext parameterContext, ExtensionContext extensionContext) throws
        ParameterResolutionException {
            return getToken();
        }

        public static void setToken (String token){
            TestMethodContextExtension.context().getStore(NAMESPACE).put("token", token);
        }

        @Nullable
        public static String getToken () {
            return TestMethodContextExtension.context().getStore(NAMESPACE).get("token", String.class);
        }

        public static void setCode (String code) {
            TestMethodContextExtension.context().getStore(NAMESPACE).put("code", code);
        }

        @Nullable
        public static String getCode () {
            return TestMethodContextExtension.context().getStore(NAMESPACE).get("code", String.class);
        }

        public static Cookie getJsessionIdCookie () {
            return new Cookie(
                    "JSESSIONID",
                    ThreadSafeCookieStore.INSTANCE.cookieValue("JSESSIONID"));
        }
    }
