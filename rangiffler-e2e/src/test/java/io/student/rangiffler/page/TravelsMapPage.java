package io.student.rangiffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class TravelsMapPage {

    public SelenideElement worldMap = $(".worldmap__figure-container");
    public SelenideElement peopleBtn = $("[data-testid='PersonSearchRoundedIcon']");

    public TravelsMapPage checkTravelPageIsOpen() {
        worldMap.shouldBe(visible);
        return this;
    }

    public PeoplePage clickPeopleBtn() {
        peopleBtn.click();
        return new PeoplePage();
    }
}
