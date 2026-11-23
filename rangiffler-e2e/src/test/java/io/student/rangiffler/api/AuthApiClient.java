package io.student.rangiffler.api;

import com.fasterxml.jackson.databind.JsonNode;
import io.qameta.allure.Step;
import io.student.rangiffler.api.core.CodeInterceptor;
import io.student.rangiffler.api.core.ThreadSafeCookieStore;
import io.student.rangiffler.config.Config;
import io.student.rangiffler.exceptions.BrokenTestException;
import io.student.rangiffler.jupiter.extension.ApiLoginExtension;
import io.student.rangiffler.utils.OAuthUtills;
import lombok.SneakyThrows;
import retrofit2.Response;

import java.io.IOException;

public class AuthApiClient extends RestClient {

    private static final Config CFG = Config.getInstance();
    private static final String XSRF_COOKIE_NAME = "XSRF-TOKEN";
    private final AuthApi authApi;

    public AuthApiClient() {
        super(CFG.authUrl(), true, new CodeInterceptor());
        this.authApi = retrofit.create(AuthApi.class);
    }


    @Step("Зарегистрировать нового пользователя")
    public void registerUser(String username, String password) {
        try {
            authApi.requestRegisterForm().execute();
            authApi.register(
                    username,
                    password,
                    password,
                    ThreadSafeCookieStore.INSTANCE.cookieValue(XSRF_COOKIE_NAME)).execute();
        } catch (IOException e) {
            throw new BrokenTestException("Can`t register user %s with password %s".formatted(username, password));
        }

    }

    public Response<Void> authorizeUser(String codeChallenge) throws IOException {
        return authApi.authorize(
                "code",
                "client",
                "openid",
                CFG.frontUrl() + "/authorized",
                codeChallenge,
                "S256").execute();

    }

    @SneakyThrows
    @Step("Залогиниться через API")
    public String login(String username, String password) {
        final String codeVerifier = OAuthUtills.generateCodeVerifier();
        final String codeChallenge = OAuthUtills.generateCodeChallenge(codeVerifier);
        final String redirectUri = CFG.frontUrl() + "/authorized";
        var clientId = "client";

        authApi.authorize("code",
                clientId,
                "openid",
                redirectUri,
                codeChallenge,
                "S256").execute();
        var loginResponse = authApi.login(username, password, ThreadSafeCookieStore.INSTANCE.cookieValue(XSRF_COOKIE_NAME)).execute();

        Response<JsonNode> tokenResponse = authApi.token(
                clientId,
                redirectUri,
                "authorization_code",
                ApiLoginExtension.getCode(),
                codeVerifier).execute();

        return tokenResponse.body().get("id_token").asText();
    }
}
