package io.student.rangiffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class RegisterPage {
    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement passwordSubmitInput = $("#passwordSubmit");
    private final SelenideElement signUpBtn = $(".form__submit");
    private final SelenideElement signInBtn = $(".form_sign-in");
    private final SelenideElement passwordsNotEqualText = $(".form__error");

    public RegisterPage registerNewUser(String username, String password) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        passwordSubmitInput.setValue(password);
        signUpBtn.click();
        return this;
    }

    public LoginPage signIn() {
        signInBtn.click();
        return new LoginPage();
    }

    public RegisterPage clickSignUpButton() {
        signUpBtn.click();
        return this;
    }

    public void checkRegistrationFailed(String username) {
        usernameInput.shouldBe(visible).shouldHave(exactValue(username));
        passwordInput.shouldBe(visible, empty);
        passwordSubmitInput.shouldBe(visible, empty);
        signUpBtn.shouldBe(visible);
    }

    public RegisterPage checkPasswordsShouldBeEqualErrorMessage() {
        passwordsNotEqualText.shouldHave(text("Passwords should be equal"));
        return this;
    }

    public RegisterPage setUsername(String username) {
        usernameInput.setValue(username);
        return this;
    }

    public RegisterPage setPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    public RegisterPage setPasswordSubmit(String password) {
        passwordSubmitInput.setValue(password);
        return this;
    }



}
