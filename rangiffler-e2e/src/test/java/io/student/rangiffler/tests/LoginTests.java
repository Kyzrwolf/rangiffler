package io.student.rangiffler.tests;

import com.codeborne.selenide.Selenide;
import com.fasterxml.jackson.databind.JsonNode;
import io.student.rangiffler.api.AuthApiClient;
import io.student.rangiffler.config.Config;
import io.student.rangiffler.jupiter.annotation.UserType;
import io.student.rangiffler.jupiter.extension.UserExtension;
import io.student.rangiffler.page.LoginPage;
import io.student.rangiffler.utils.OAuthUtills;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import retrofit2.Response;

import javax.annotation.Nonnull;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LoginTests extends BaseTest {
    private static final Config CFG = Config.getInstance();
    AuthApiClient authApiClient = new AuthApiClient();

    @Test
    @DisplayName("Успешная авторизация")
    void mainPageShouldBeDisplayedAfterSuccessfulLogin(@UserType() @Nonnull UserExtension.TestUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickLoginBtn()
                .login(user.username(), user.password())
                .checkTravelPageIsOpen();
    }

    @Test
    @DisplayName("|-| Неуспешная авторизация с неправильным паролем")
    public void userShouldStayOnLoginPageAfterLoginWithBadCredentials(@UserType() @Nonnull UserExtension.TestUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickLoginBtn()
                .setUsername(user.username())
                .setPassword(user.password() + " ")
                .clickSignInBtn()
                .checkBadCredentialsErrorIsDisplayed();
    }

    @Test
    public void fakeLoginTest() throws IOException {
        var codeVerifier = OAuthUtills.generateCodeVerifier();
        var codeChallenge = OAuthUtills.generateCodeChallenge(codeVerifier);

        authApiClient.authorizeUser(codeChallenge);
        authApiClient.login("Bob", "123");
        var code = authApiClient.authorizeUser(codeChallenge).raw().headers().get("Location").split("code=")[1];

        Response<JsonNode> response = authApiClient.token(code, codeVerifier);
        var accessToken = response.body().get("access_token").asText();
        assertNotNull(accessToken);
        assertFalse(accessToken.isEmpty());
    }
}
