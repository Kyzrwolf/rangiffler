package io.student.rangiffler.api;

import com.fasterxml.jackson.databind.JsonNode;
import io.qameta.allure.Step;
import io.student.rangiffler.api.core.ThreadSafeCookieStore;
import io.student.rangiffler.exceptions.BrokenTestException;
import retrofit2.Response;

import java.io.IOException;

public class AuthApiClient extends RestClient {

    private final AuthApi authApi;

    public AuthApiClient() {
        super(CFG.authUrl());
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
                    ThreadSafeCookieStore.INSTANCE.cookieValue("XSRF-TOKEN")).execute();
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

    public Response<Void> login(String username, String password) throws IOException {
        authApi.requestLoginForm().execute();
        return authApi.login(username, password, ThreadSafeCookieStore.INSTANCE.cookieValue("XSRF-TOKEN")).execute();
    }

    public Response<JsonNode> token(String codeChallenge, String codeVerifier) throws IOException {
        return authApi.token("client",
                "http://localhost:3001/authorized",
                "authorization_code",
                codeChallenge,
                codeVerifier).execute();
    }
}
