package io.student.rangiffler.page.component;

import com.codeborne.selenide.SelenideElement;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class AlertComponent {
    public SelenideElement alert = $("[role='alert']");

    @Nonnull
    public AlertComponent checkText(@Nonnull String text) {
        alert.shouldHave(text(text));
        return this;
    }
}
