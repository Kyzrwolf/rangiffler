package io.student.rangiffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class RegisterPage {
    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement passwordSubmitInput = $("#passwordSubmit");
    private final SelenideElement signUpBtn = $(".form__submit");
    private final SelenideElement signInBtn = $(".form_sign-in");
    private final SelenideElement registerSuccessText = $(".form__paragraph form__paragraph_success");

    public LoginPage registerNewUser(String username, String password) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        passwordSubmitInput.setValue(password);
        signUpBtn.click();
        signInBtn.click();
        return new LoginPage();
    }

}
