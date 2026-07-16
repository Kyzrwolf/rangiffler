package io.student.rangiffler.api;

import io.student.rangiffler.api.core.ThreadSafeCookieStore;

import java.io.IOException;

public class AuthApiClient extends RestClient {

    private final AuthApi authApi;

    public AuthApiClient() {
        super(CFG.authUrl());
        this.authApi = retrofit.create(AuthApi.class);
    }

    public void registerUser(String username, String password) throws IOException {
        authApi.requestRegisterForm().execute();
        authApi.register(
                username,
                password,
                password,
                ThreadSafeCookieStore.INSTANCE.cookieValue("XSRF-TOKEN")).execute();
    }
}
