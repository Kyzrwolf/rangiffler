package io.student.rangiffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class AuthPage {

    private final SelenideElement usernameInput = $("[name='username']");
    private final SelenideElement passwordInput = $("[name='password']");
    private final SelenideElement signInBtn = $(".form__submit");
    private final SelenideElement badCredentialsError = $(".form__error");

    public TravelsMapPage login(String username, String password) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        signInBtn.click();
        return new TravelsMapPage();
    }

    public AuthPage setUsername(String username) {
        usernameInput.setValue(username);
        return this;
    }

    public AuthPage setPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    public AuthPage clickSignInBtn() {
        signInBtn.click();
        return this;
    }

    public void checkBadCredentialsErrorIsDisplayed() {
        badCredentialsError.shouldHave(text("Bad credentials"));
    }
}
