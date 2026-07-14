package io.student.rangiffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ProfilePage {

    public SelenideElement firstNameInput = $("#firstname");
    public SelenideElement surNameInput = $("#surname");
    public SelenideElement avatarInput = $("#image__input");
    public SelenideElement locationDropDown = $("#location");
    public SelenideElement saveBtn = $("[type='submit']");

    @Step("Установить имя '{firstName}'")
    @Nonnull
    public ProfilePage setFirstName(@Nonnull String firstName) {
        firstNameInput.setValue(firstName);
        return this;
    }
    @Step("Установить фамилию '{lastName}'")
    @Nonnull
    public ProfilePage setSurname(@Nonnull String lastName) {
        surNameInput.setValue(lastName);
        return this;
    }

    @Step("Установить местоположение '{location}'")
    @Nonnull
    public ProfilePage setLocation(@Nonnull String location) {
        locationDropDown.click();
        $$("li[role='option']").findBy(text(location)).click();
        return this;
    }
    @Step("Загрузить новый аватар '{fileName}'")
    @Nonnull
    public ProfilePage uploadNewAvatar(@Nonnull String fileName) {
        avatarInput.uploadFromClasspath(fileName);
        return this;
    }

    @Step("Нажать кнопку сохранения")
    @Nonnull
    public ProfilePage clickSaveBtn() {
        saveBtn.click();
        return this;
    }

    @Step("Проверить, что имя равно '{firstName}'")
    @Nonnull
    public ProfilePage checkFirstName(@Nonnull String firstName) {
        firstNameInput.shouldHave(value(firstName));
        return this;
    }

    @Step("Проверить, что фамилия равна '{lastName}'")
    @Nonnull
    public ProfilePage checkSurname(@Nonnull String lastName) {
        surNameInput.shouldHave(value(lastName));
        return this;
    }

    @Step("Проверить, что местоположение равно '{location}'")
    @Nonnull
    public ProfilePage checkLocation(@Nonnull String location) {
        locationDropDown.shouldHave(text(location));
        return this;
    }

    @Step("Проверить аватар '{fileName}'")
    @Nonnull
    public ProfilePage checkAvatar(@Nonnull String fileName) {
        avatarInput.shouldHave(text(fileName));
        return this;
    }

}
