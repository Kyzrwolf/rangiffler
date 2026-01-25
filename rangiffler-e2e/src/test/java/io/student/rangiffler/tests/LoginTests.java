package io.student.rangiffler.tests;

import com.codeborne.selenide.Selenide;
import io.student.rangiffler.config.Config;
import io.student.rangiffler.jupiter.annotation.User;
import io.student.rangiffler.models.UserJson;
import io.student.rangiffler.page.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LoginTests extends BaseTest {
    private static final Config CFG = Config.getInstance();

    @Test
    @User
    @DisplayName("Успешная авторизация")
    void mainPageShouldBeDisplayedAfterSuccessfulLogin(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickLoginBtn()
                .login(user.username(), user.password())
                .checkTravelPageIsOpen();
    }

    @Test
    @User
    @DisplayName("|-| Неуспешная авторизация с неправильным паролем")
    public void userShouldStayOnLoginPageAfterLoginWithBadCredentials(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickLoginBtn()
                .setUsername(user.username())
                .setPassword(user.password() + " ")
                .clickSignInBtn()
                .checkBadCredentialsErrorIsDisplayed();
    }
}
