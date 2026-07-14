package io.student.rangiffler.page;

import com.codeborne.selenide.SelenideElement;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class TravelsMapPage {

    public SelenideElement worldMap = $(".worldmap__figure-container");
    public SelenideElement peopleBtn = $("[data-testid='PersonSearchRoundedIcon']");
    public SelenideElement profileBtn = $("[data-testid='AccountCircleRoundedIcon']");

    @Nonnull
    public TravelsMapPage checkTravelPageIsOpen() {
        worldMap.shouldBe(visible);
        return this;
    }

    @Nonnull
    public PeoplePage clickPeopleBtn() {
        peopleBtn.click();
        return new PeoplePage();
    }

    @Nonnull
    public ProfilePage clickProfileBtn() {
        profileBtn.click();
        return new ProfilePage();
    }
}
