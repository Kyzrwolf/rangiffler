package io.student.rangiffler.tests;

import com.codeborne.selenide.Selenide;
import io.student.rangiffler.config.Config;
import io.student.rangiffler.page.RegisterPage;
import net.datafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RegistrationPageTests extends BaseTest {

    private static final Config CFG = Config.getInstance();
    private Faker faker = new Faker();

    @Test
    @DisplayName("Регистрация нового пользователя")
    public void shouldRegisterNewUser() {
        var username = faker.credentials().username();
        var password = faker.credentials().password(3,12);

        Selenide.open(CFG.registerUrl(), RegisterPage.class)
                .registerNewUser(username, password)
                .clickLoginBtn()
                .login(username, password)
                .checkMainPageIsOpen();
    }


}
