package io.student.rangiffler.page;

import com.codeborne.selenide.SelenideElement;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class RegisterPage {
    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement passwordSubmitInput = $("#passwordSubmit");
    private final SelenideElement signUpBtn = $(".form__submit");
    private final SelenideElement signInBtn = $(".form_sign-in");
    private final SelenideElement passwordsNotEqualText = $(".form__error");

    @Nonnull
    public RegisterPage registerNewUser(@Nonnull String username, @Nonnull String password) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        passwordSubmitInput.setValue(password);
        signUpBtn.click();
        return this;
    }

    @Nonnull
    public LoginPage signIn() {
        signInBtn.click();
        return new LoginPage();
    }

    @Nonnull
    public RegisterPage clickSignUpButton() {
        signUpBtn.click();
        return this;
    }

    public void checkRegistrationFailed(@Nonnull String username) {
        usernameInput.shouldBe(visible).shouldHave(exactValue(username));
        passwordInput.shouldBe(visible, empty);
        passwordSubmitInput.shouldBe(visible, empty);
        signUpBtn.shouldBe(visible);
    }

    @Nonnull
    public RegisterPage checkPasswordsShouldBeEqualErrorMessage() {
        passwordsNotEqualText.shouldHave(text("Passwords should be equal"));
        return this;
    }

    @Nonnull
    public RegisterPage setUsername(@Nonnull String username) {
        usernameInput.setValue(username);
        return this;
    }

    @Nonnull
    public RegisterPage setPassword(@Nonnull String password) {
        passwordInput.setValue(password);
        return this;
    }

    @Nonnull
    public RegisterPage setPasswordSubmit(@Nonnull String password) {
        passwordSubmitInput.setValue(password);
        return this;
    }



}
