package io.student.rangiffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public abstract class BasePage<T extends BasePage<?>> {

    private final SelenideElement alert = $(".MuiAlert-message");

    @Nonnull
    @Step("Проверить, что алерт содержит текст '{text}'")
    public T checkAlertMessage(String text) {
        alert.should(Condition.text(text));
        return (T) this;
    }
}
