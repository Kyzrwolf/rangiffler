package io.student.rangiffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class TravelsMapPage extends BasePage<TravelsMapPage> {

    public SelenideElement worldMap = $(".worldmap__figure-container");
    public SelenideElement peopleBtn = $("[data-testid='PersonSearchRoundedIcon']");
    public SelenideElement profileBtn = $("[data-testid='AccountCircleRoundedIcon']");

    @Step("Проверить, что страница путешествий открыта")
    @Nonnull
    public TravelsMapPage checkTravelPageIsOpen() {
        worldMap.shouldBe(visible);
        return this;
    }

    @Step("Нажать кнопку 'Люди'")
    @Nonnull
    public PeoplePage clickPeopleBtn() {
        peopleBtn.click();
        return new PeoplePage();
    }

    @Step("Нажать кнопку 'Профиль'")
    @Nonnull
    public ProfilePage clickProfileBtn() {
        profileBtn.click();
        return new ProfilePage();
    }
}
