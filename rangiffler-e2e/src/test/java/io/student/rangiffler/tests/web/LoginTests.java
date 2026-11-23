package io.student.rangiffler.tests.web;

import com.codeborne.selenide.Selenide;
import io.student.rangiffler.config.Config;
import io.student.rangiffler.jupiter.annotation.UserType;
import io.student.rangiffler.jupiter.extension.UserExtension.TestUser;
import io.student.rangiffler.page.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class LoginTests extends BaseTest {
    private static final Config CFG = Config.getInstance();

    @Test
    @DisplayName("Успешная авторизация")
    void mainPageShouldBeDisplayedAfterSuccessfulLogin(@UserType() TestUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickLoginBtn()
                .login(user.username(), user.password())
                .checkTravelPageIsOpen();
    }

    @Test
    @DisplayName("|-| Неуспешная авторизация с неправильным паролем")
    public void userShouldStayOnLoginPageAfterLoginWithBadCredentials(@UserType() TestUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickLoginBtn()
                .setUsername(user.username())
                .setPassword(user.password() + " ")
                .clickSignInBtn()
                .checkBadCredentialsErrorIsDisplayed();
    }
}
