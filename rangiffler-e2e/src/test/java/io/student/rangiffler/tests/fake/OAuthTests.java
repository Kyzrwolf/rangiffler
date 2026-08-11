package io.student.rangiffler.tests.fake;

import io.student.rangiffler.jupiter.annotation.ApiLogin;
import io.student.rangiffler.jupiter.annotation.Token;
import io.student.rangiffler.jupiter.annotation.UserType;
import io.student.rangiffler.jupiter.extension.UserExtension;
import io.student.rangiffler.jupiter.extension.UserExtension.TestUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.student.rangiffler.jupiter.annotation.UserType.Type.EMPTY;
import static io.student.rangiffler.jupiter.annotation.UserType.Type.WITH_FRIEND;

@ExtendWith(UserExtension.class)
public class OAuthTests {

    @Test
    @DisplayName("Api Login with generated user")
    @ApiLogin
    public void fakeLoginTest2(@UserType(EMPTY) TestUser user, @Token String token) {
        Assertions.assertNotNull(token);
        Assertions.assertNotNull(user);
        Assertions.assertNotNull(user.username());
        Assertions.assertNotNull(user.password());
    }

    @Test
    @DisplayName("Api Login with generated user with friends")
    @ApiLogin
    public void fakeLoginTest3(@UserType(WITH_FRIEND) TestUser user, @Token String token) {
        Assertions.assertNotNull(user.friend());
        Assertions.assertNotNull(user.friend().username());
    }
}
