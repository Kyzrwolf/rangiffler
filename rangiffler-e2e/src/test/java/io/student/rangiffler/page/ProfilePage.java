package io.student.rangiffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@ParametersAreNonnullByDefault
public class ProfilePage extends BasePage<ProfilePage> {

    public SelenideElement firstNameInput = $("#firstname");
    public SelenideElement surNameInput = $("#surname");
    public SelenideElement avatarInput = $("#image__input");
    public SelenideElement usernameInput = $("#username");
    public SelenideElement locationDropDown = $("#location");
    public SelenideElement saveBtn = $("[type='submit']");

    @Step("Установить имя '{firstName}'")
    @Nonnull
    public ProfilePage setFirstName(String firstName) {
        firstNameInput.setValue(firstName);
        return this;
    }
    @Step("Установить фамилию '{surname}'")
    @Nonnull
    public ProfilePage setSurname(String surname) {
        surNameInput.setValue(surname);
        return this;
    }

    @Step("Установить местоположение '{location}'")
    @Nonnull
    public ProfilePage setLocation(String location) {
        locationDropDown.click();
        $$("li[role='option']").findBy(text(location)).click();
        return this;
    }
    @Step("Загрузить новый аватар '{fileName}'")
    @Nonnull
    public ProfilePage uploadNewAvatar(String fileName) {
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
    public ProfilePage checkFirstName(String firstName) {
        firstNameInput.shouldHave(value(firstName));
        return this;
    }

    @Step("Проверить, что фамилия равна '{lastName}'")
    @Nonnull
    public ProfilePage checkSurname(String lastName) {
        surNameInput.shouldHave(value(lastName));
        return this;
    }

    @Step("Проверить, что местоположение равно '{location}'")
    @Nonnull
    public ProfilePage checkLocation(String location) {
        locationDropDown.shouldHave(text(location));
        return this;
    }

    @Step("Проверить, что username равен '{username}'")
    @Nonnull
    public ProfilePage checkUsername(String username) {
        usernameInput.shouldHave(value(username));
        return this;
    }

}
