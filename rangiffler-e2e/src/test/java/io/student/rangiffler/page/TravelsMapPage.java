package io.student.rangiffler.page;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.ElementsCollection;
import io.qameta.allure.Step;
import io.student.rangiffler.page.component.PhotoModalComponent;
import io.student.rangiffler.page.component.PhotoCardComponent;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@ParametersAreNonnullByDefault
public class TravelsMapPage extends BasePage<TravelsMapPage> {

    public SelenideElement worldMap = $(".worldmap__figure-container");
    public SelenideElement peopleBtn = $("[data-testid='PersonSearchRoundedIcon']");
    public SelenideElement profileBtn = $("[data-testid='AccountCircleRoundedIcon']");
    private final SelenideElement addPhotoBtn = $(byText("Add photo"));
    private final SelenideElement pageTitle = $("h2");
    private final SelenideElement mapSvg = worldMap.$("svg");
    private final ElementsCollection photoCards = $$(".MuiGrid-item > .MuiPaper-root");
    private final SelenideElement feedLoader = $("[role='progressbar']");

    @Step("Открыть форму добавления фотографии")
    @Nonnull
    public PhotoModalComponent clickAddPhoto() {
        addPhotoBtn.click();
        return new PhotoModalComponent();
    }

    @Step("Проверить фотографию '{description}' из страны '{country}'")
    @Nonnull
    public TravelsMapPage checkPhotoPresent(String description, String country) {
        photoCard(description).checkPhoto(description, country);
        return this;
    }

    @Step("Подготовить карту путешествий к снимку")
    @Nonnull
    public TravelsMapPage prepareMapScreenshot() {
        worldMap.shouldBe(visible);
        mapSvg.shouldBe(visible);
        // Hovering a country changes its border and may open a tooltip.
        pageTitle.shouldHave(text("Travels map")).hover();
        return this;
    }

    @Step("Удалить фотографию '{description}', если она была сохранена")
    @Nonnull
    public TravelsMapPage deletePhotoIfPresent(String description) {
        feedLoader.shouldNot(exist);
        PhotoCardComponent card = photoCard(description);
        if (card.isPresent()) {
            card.delete();
            checkAlertMessage("Post deleted");
        }
        return this;
    }

    private PhotoCardComponent photoCard(String description) {
        return new PhotoCardComponent(photoCards.findBy(text(description)));
    }

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
