package io.student.rangiffler.page;

import com.codeborne.selenide.SelenideElement;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

    public SelenideElement loginBtn = $(byText("Login"));

    @Nonnull
    public AuthPage clickLoginBtn() {
        loginBtn.click();
        return new AuthPage();
    }
}
