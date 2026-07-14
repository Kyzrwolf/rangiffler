package io.student.rangiffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class AuthPage {

    private final SelenideElement usernameInput = $("[name='username']");
    private final SelenideElement passwordInput = $("[name='password']");
    private final SelenideElement signInBtn = $(".form__submit");
    private final SelenideElement badCredentialsError = $(".form__error");

    @Step("Авторизоваться под пользователем '{username}'")
    @Nonnull
    public TravelsMapPage login(@Nonnull String username, @Nonnull String password) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        signInBtn.click();
        return new TravelsMapPage();
    }


    @Step("Установить имя пользователя '{username}'")
    @Nonnull
    public AuthPage setUsername(@Nonnull String username) {
        usernameInput.setValue(username);
        return this;
    }

    @Step("Установить пароль")
    @Nonnull
    public AuthPage setPassword(@Nonnull String password) {
        passwordInput.setValue(password);
        return this;
    }

    @Step("Нажать кнопку входа")
    @Nonnull
    public AuthPage clickSignInBtn() {
        signInBtn.click();
        return this;
    }

    @Step("Проверить отображение ошибки неверных учетных данных")
    public void checkBadCredentialsErrorIsDisplayed() {
        badCredentialsError.shouldHave(text("Bad credentials"));
    }
}
