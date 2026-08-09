package io.student.rangiffler.service;

import io.student.rangiffler.model.UserJson;

import javax.annotation.Nonnull;

public interface UsersClient {
    @Nonnull
    UserJson createUser(@Nonnull String username, @Nonnull String password);

    @Nonnull
    UserJson findByUsername(@Nonnull String username);

    void addFriendship(@Nonnull UserJson requester, @Nonnull UserJson addressee);

    void addPendingRequest(@Nonnull UserJson requester, @Nonnull UserJson addressee);

    void deleteUser(@Nonnull UserJson user);
}
