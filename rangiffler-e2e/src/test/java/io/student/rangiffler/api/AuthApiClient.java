package io.student.rangiffler.api;

import io.qameta.allure.Step;
import io.student.rangiffler.api.core.ThreadSafeCookieStore;
import io.student.rangiffler.exceptions.BrokenTestException;

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
}
