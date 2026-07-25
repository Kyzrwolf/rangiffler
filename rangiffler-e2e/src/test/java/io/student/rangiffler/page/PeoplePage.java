package io.student.rangiffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@ParametersAreNonnullByDefault
public class PeoplePage extends BasePage<PeoplePage> {
    public SelenideElement peopleTabs = $("div[role='tablist'][aria-label='People tabs']");
    public SelenideElement friendsBtn = peopleTabs.$$("button")
            .findBy(text("Friends"));
    public SelenideElement outcomeInvitationsBtn = peopleTabs.$$("button")
            .findBy(text("Outcome invitations"));
    public SelenideElement incomeInvitationsBtn = peopleTabs.$$("button")
            .findBy(text("Income invitations"));
    public SelenideElement tabPanelFriends = $("#simple-tabpanel-friends");
    public SelenideElement searchInput = $(".MuiInputBase-input");
    public SelenideElement searchBtn = $("[data-testid='SearchIcon']");
    public ElementsCollection peopleList = $$("tr");

    @Step("Нажать вкладку 'Друзья'")
    @Nonnull
    public PeoplePage clickFriendsBtn() {
        friendsBtn.click();
        return this;
    }

    @Step("Нажать вкладку 'Все'")
    @Nonnull
    public PeoplePage clickAllBtn() {
        peopleTabs.$$("button").findBy(text("All")).click();
        return this;
    }

    @Step("Нажать вкладку 'Исходящие заявки'")
    @Nonnull
    public PeoplePage clickOutcomeInvitationsBtn() {
        outcomeInvitationsBtn.click();
        return this;
    }

    @Step("Нажать вкладку 'Входящие заявки'")
    @Nonnull
    public PeoplePage clickIncomeInvitationsBtn() {
        incomeInvitationsBtn.click();
        return this;
    }

    @Step("Проверить, что пользователь '{username}' присутствует в списке")
    public void checkUserIsPresentInPeopleList(String username) {
        peopleList.findBy(text(username))
                .shouldBe(visible);
    }

    @Step("Проверить, что пользователь '{username}' отсутствует в списке")
    public void checkUserIsNotPresentInPeopleList(String username) {
        peopleList.findBy(text(username))
                .shouldNotBe(visible);
    }

    @Step("Проверить, что список пользователей пуст")
    public void checkPeopleListIsEmpty() {
        tabPanelFriends.shouldHave(text("There are no users yet"));
        peopleList.shouldHave(size(1));
    }

    @Step("Нажать кнопку 'Добавить' для пользователя '{username}'")
    @Nonnull
    public PeoplePage clickAddButton(String username) {
        getRow(username).$$("button").findBy(text("Add")).click();
        return this;
    }

    @Step("Нажать кнопку 'Принять' для пользователя '{username}'")
    @Nonnull
    public PeoplePage clickAcceptButton(String username) {
        getRow(username).$$("button").findBy(text("Accept")).click();
        return this;
    }

    @Step("Нажать кнопку 'Отклонить' для пользователя '{username}'")
    @Nonnull
    public PeoplePage clickDeclineButton(String username) {
        getRow(username).$$("button").findBy(text("Decline")).click();
        return this;
    }

    @Step("Удалить пользователя '{username}' из друзей")
    @Nonnull
    public PeoplePage removeUserFromFriends(String username) {
        getRow(username).$$("button").findBy(text("remove")).click();
        return this;
    }
    @Step
    public PeoplePage searchPerson(String username) {
        searchInput.setValue(username);
        searchBtn.click();
        return this;
    }

    @Nonnull
    private SelenideElement getRow(String username) {
        return peopleList.findBy(text(username));
    }
}
