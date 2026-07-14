package io.student.rangiffler.page;

import com.codeborne.selenide.SelenideElement;

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

    @Nonnull
    public ProfilePage setFirstName(@Nonnull String firstName) {
        firstNameInput.setValue(firstName);
        return this;
    }
    @Nonnull
    public ProfilePage setSurname(@Nonnull String lastName) {
        surNameInput.setValue(lastName);
        return this;
    }

    @Nonnull
    public ProfilePage setLocation(@Nonnull String location) {
        locationDropDown.click();
        $$("li[role='option']").findBy(text(location)).click();
        return this;
    }
    @Nonnull
    public ProfilePage uploadNewAvatar(@Nonnull String fileName) {
        avatarInput.uploadFromClasspath(fileName);
        return this;
    }

    @Nonnull
    public ProfilePage clickSaveBtn() {
        saveBtn.click();
        return this;
    }

    @Nonnull
    public ProfilePage checkFirstName(@Nonnull String firstName) {
        firstNameInput.shouldHave(value(firstName));
        return this;
    }

    @Nonnull
    public ProfilePage checkSurname(@Nonnull String lastName) {
        surNameInput.shouldHave(value(lastName));
        return this;
    }

    @Nonnull
    public ProfilePage checkLocation(@Nonnull String location) {
        locationDropDown.shouldHave(text(location));
        return this;
    }

    @Nonnull
    public ProfilePage checkAvatar(@Nonnull String fileName) {
        avatarInput.shouldHave(text(fileName));
        return this;
    }

}
