package io.student.rangiffler.service;

import io.student.rangiffler.models.UserJson;

public interface UsersClient {
    UserJson createUser(String username, String password);
}
