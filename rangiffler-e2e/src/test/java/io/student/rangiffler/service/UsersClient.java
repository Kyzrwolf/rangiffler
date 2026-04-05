package io.student.rangiffler.service;

import io.student.rangiffler.models.UserJson;

public interface UsersClient {
    UserJson createUser(String username, String password);

    UserJson findByUsername(String username);

    void addFriendship(UserJson requester, UserJson addressee);

    void addPendingRequest(UserJson requester, UserJson addressee);

    void deleteUser(UserJson user);
}
