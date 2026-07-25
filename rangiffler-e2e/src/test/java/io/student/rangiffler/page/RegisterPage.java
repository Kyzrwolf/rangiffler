package io.student.rangiffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class RegisterPage {
    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement passwordSubmitInput = $("#passwordSubmit");
    private final SelenideElement signUpBtn = $(".form__submit");
    private final SelenideElement signInBtn = $(".form_sign-in");
    private final SelenideElement passwordsNotEqualText = $(".form__error");

    @Step("Зарегистрировать нового пользователя '{username}'")
    @Nonnull
    public RegisterPage registerNewUser(String username, String password) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        passwordSubmitInput.setValue(password);
        signUpBtn.click();
        return this;
    }

    @Step("Перейти на страницу входа")
    @Nonnull
    public LoginPage signIn() {
        signInBtn.click();
        return new LoginPage();
    }

    @Step("Нажать кнопку регистрации")
    @Nonnull
    public RegisterPage clickSignUpButton() {
        signUpBtn.click();
        return this;
    }

    @Step("Проверить, что регистрация пользователя '{username}' не удалась")
    public void checkRegistrationFailed(String username) {
        usernameInput.shouldBe(visible).shouldHave(exactValue(username));
        passwordInput.shouldBe(visible, empty);
        passwordSubmitInput.shouldBe(visible, empty);
        signUpBtn.shouldBe(visible);
    }

    @Step("Проверить сообщение об ошибке несовпадения паролей")
    @Nonnull
    public RegisterPage checkPasswordsShouldBeEqualErrorMessage() {
        passwordsNotEqualText.shouldHave(text("Passwords should be equal"));
        return this;
    }

    @Step("Установить имя пользователя '{username}'")
    @Nonnull
    public RegisterPage setUsername(String username) {
        usernameInput.setValue(username);
        return this;
    }

    @Step("Установить пароль")
    @Nonnull
    public RegisterPage setPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    @Step("Установить подтверждение пароля")
    @Nonnull
    public RegisterPage setPasswordSubmit(String password) {
        passwordSubmitInput.setValue(password);
        return this;
    }


}
