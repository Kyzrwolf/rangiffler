package io.student.rangiffler.page.component;

import com.codeborne.selenide.SelenideElement;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;

@ParametersAreNonnullByDefault
public class PhotoCardComponent {
    private final SelenideElement card;
    private final SelenideElement photo;
    private final SelenideElement deleteBtn;

    public PhotoCardComponent(SelenideElement card) {
        this.card = card;
        this.photo = card.$(".photo-card__image");
        this.deleteBtn = card.$(byText("Delete"));
    }

    public void checkPhoto(String description, String country) {
        photo.shouldBe(visible, image).shouldHave(attribute("alt", description));
        card.shouldHave(text(country), text(description));
    }

    public boolean isPresent() {
        return card.exists();
    }

    public void delete() {
        deleteBtn.click();
        card.shouldNot(exist);
    }
}
