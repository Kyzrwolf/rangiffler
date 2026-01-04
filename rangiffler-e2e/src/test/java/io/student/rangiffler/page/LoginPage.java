package io.student.rangiffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

    public SelenideElement loginBtn = $(byText("Login"));
    public SelenideElement registerBtn = $(byText("Register"));

    public RegisterPage clickRegisterBtn() {
        registerBtn.click();
        return new RegisterPage();
    }

    public AuthPage clickLoginBtn() {
        loginBtn.click();
        return new AuthPage();
    }
}
