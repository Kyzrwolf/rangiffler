package io.student.rangiffler.tests.api;

import io.student.rangiffler.api.InternalApiClient;
import io.student.rangiffler.jupiter.annotation.ApiLogin;
import io.student.rangiffler.jupiter.annotation.Token;
import io.student.rangiffler.jupiter.annotation.UserType;
import io.student.rangiffler.jupiter.extension.UserExtension;
import io.student.rangiffler.jupiter.extension.UserExtension.TestUser;
import io.student.rangiffler.model.types.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Isolated;

import static io.student.rangiffler.jupiter.annotation.UserType.Type.EMPTY;
import static io.student.rangiffler.jupiter.annotation.UserType.Type.WITH_FRIEND;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(UserExtension.class)
@Isolated
public class UsersApiTests {

    private final InternalApiClient apiClient = new InternalApiClient();

    @Test
    @DisplayName("Список друзей пуст у пользователя без друзей")
    @Order(1)
    @ApiLogin(setupBrowser = false)
    void friendsListShouldBeEmpty(@UserType(EMPTY) TestUser user, @Token String token) {
        assertThat(apiClient.friends(token, 0, 10)).isEmpty();
    }

    @Test
    @DisplayName("Список друзей содержит друга пользователя")
    @ApiLogin(setupBrowser = false)
    @Order(Integer.MAX_VALUE)
    void friendsListShouldContainFriend(@UserType(WITH_FRIEND) TestUser user, @Token String token) {
        assertThat(apiClient.friends(token, 0, 10))
                .extracting(User::getUsername)
                .containsExactly(user.friend().username());
    }
}