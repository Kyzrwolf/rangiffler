package io.student.rangiffler.page;

import com.codeborne.selenide.SelenideElement;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class AuthPage {

    private final SelenideElement usernameInput = $("[name='username']");
    private final SelenideElement passwordInput = $("[name='password']");
    private final SelenideElement signInBtn = $(".form__submit");
    private final SelenideElement badCredentialsError = $(".form__error");

    @Nonnull
    public TravelsMapPage login(@Nonnull String username, @Nonnull String password) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        signInBtn.click();
        return new TravelsMapPage();
    }


    @Nonnull
    public AuthPage setUsername(@Nonnull String username) {
        usernameInput.setValue(username);
        return this;
    }

    @Nonnull
    public AuthPage setPassword(@Nonnull String password) {
        passwordInput.setValue(password);
        return this;
    }

    @Nonnull
    public AuthPage clickSignInBtn() {
        signInBtn.click();
        return this;
    }

    public void checkBadCredentialsErrorIsDisplayed() {
        badCredentialsError.shouldHave(text("Bad credentials"));
    }
}
