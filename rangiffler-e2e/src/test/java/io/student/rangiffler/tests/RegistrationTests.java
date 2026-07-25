package io.student.rangiffler.tests;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Allure;
import io.student.rangiffler.api.AuthApiClient;
import io.student.rangiffler.config.Config;
import io.student.rangiffler.jupiter.annotation.UserType;
import io.student.rangiffler.jupiter.extension.UserExtension;
import io.student.rangiffler.page.LoginPage;
import io.student.rangiffler.page.RegisterPage;
import io.student.rangiffler.utils.RandomUtils;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.annotation.Nonnull;

@Slf4j
public class RegistrationTests extends BaseTest {

    private static final Config CFG = Config.getInstance();
    private final Faker faker = new Faker();
    private String username;
    private String password;
    private final AuthApiClient authApiClient = new AuthApiClient();

    @BeforeEach
    public void beforeEach() {
        username = faker.credentials().username();
        password = faker.credentials().password(3,12);
        log.info("username: {}, password: {}", username, password);
        Allure.parameter("username", username);
        Allure.parameter("password", password);
    }

    @Test
    @DisplayName("|-| Пользователь уже зарегистрирован")
    public void shouldNotRegisterUserWithExistingUsername(@UserType() @Nonnull UserExtension.TestUser user) {
        Selenide.open(CFG.registerUrl(), RegisterPage.class)
                .registerNewUser(user.username(), password)
                .checkRegistrationFailed(user.username());
    }

    @Test
    @DisplayName("|-| Пароли не совпадают")
    public void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
        Selenide.open(CFG.registerUrl(), RegisterPage.class)
                .setUsername(username)
                .setPassword(password)
                .setPasswordSubmit(password + RandomUtils.generateRandomAlphanumericString(6))
                .clickSignUpButton()
                .checkPasswordsShouldBeEqualErrorMessage()
                .checkRegistrationFailed(username);
    }

    @Test
    @DisplayName("Регистрация нового пользователя")
    public void shouldRegisterNewUser() {
        Selenide.open(CFG.registerUrl(), RegisterPage.class)
                .registerNewUser(username, password)
                .signIn()
                .clickLoginBtn()
                .login(username, password)
                .checkTravelPageIsOpen();
    }

    @Test
    @DisplayName("Регистрация пользователя через API")
    public void shouldRegisterUserViaApi() {
        authApiClient.registerUser(username, password);
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickLoginBtn()
                .login(username, password)
                .clickProfileBtn()
                .checkUsername(username);
    }

}
