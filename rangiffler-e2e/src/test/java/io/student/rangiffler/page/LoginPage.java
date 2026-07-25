package io.student.rangiffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class LoginPage {

    public SelenideElement loginBtn = $(byText("Login"));

    @Step("Нажать кнопку входа в систему")
    @Nonnull
    public AuthPage clickLoginBtn() {
        loginBtn.click();
        return new AuthPage();
    }
}
