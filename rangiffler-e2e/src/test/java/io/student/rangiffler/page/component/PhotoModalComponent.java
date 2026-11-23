package io.student.rangiffler.page.component;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.ElementsCollection;
import io.qameta.allure.Step;
import io.student.rangiffler.page.TravelsMapPage;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@ParametersAreNonnullByDefault
public class PhotoModalComponent {
    private final SelenideElement modal = $("[role='dialog']");
    private final SelenideElement photoInput = modal.$("#image__input");
    private final SelenideElement photoPreview = modal.$(".image-upload__image");
    private final SelenideElement countrySelect = modal.$("#country");
    private final ElementsCollection countryOptions = $$("li[role='option']");
    private final SelenideElement descriptionInput = modal.$("#description");
    private final SelenideElement saveBtn = modal.$("button[type='submit']");

    @Step("Загрузить фотографию '{fileName}'")
    @Nonnull
    public PhotoModalComponent uploadPhoto(String fileName) {
        modal.shouldBe(visible);
        photoInput.uploadFromClasspath(fileName);
        photoPreview.shouldBe(image);
        return this;
    }

    @Step("Выбрать страну фотографии '{countryCode}'")
    @Nonnull
    public PhotoModalComponent setCountry(String countryCode) {
        countrySelect.click();
        countryOptions.findBy(attribute("data-value", countryCode)).click();
        return this;
    }

    @Step("Задать описание фотографии '{description}'")
    @Nonnull
    public PhotoModalComponent setDescription(String description) {
        descriptionInput.setValue(description);
        return this;
    }

    @Step("Сохранить фотографию")
    @Nonnull
    public TravelsMapPage save() {
        saveBtn.click();
        modal.shouldBe(hidden);
        return new TravelsMapPage();
    }
}
