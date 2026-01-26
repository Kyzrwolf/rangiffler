package io.student.rangiffler.tests;

import com.codeborne.selenide.Selenide;
import io.student.rangiffler.config.Config;
import io.student.rangiffler.jupiter.annotation.User;
import io.student.rangiffler.models.UserJson;
import io.student.rangiffler.page.RegisterPage;
import net.datafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RegistrationTests extends BaseTest {

    private static final Config CFG = Config.getInstance();
    private final Faker faker = new Faker();
    private final String username = faker.credentials().username();
    private final String password = faker.credentials().password(3,12);

    @Test
    @User
    @DisplayName("|-| Пользователь уже зарегистрирован")
    public void shouldNotRegisterUserWithExistingUsername(UserJson user) {
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
                .setPasswordSubmit(password + "1")
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



}
