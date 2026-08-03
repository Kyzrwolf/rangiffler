package io.student.rangiffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class PeoplePage {
    public SelenideElement peopleTabs = $("div[role='tablist'][aria-label='People tabs']");
    public SelenideElement friendsBtn = peopleTabs.$$("button")
            .findBy(text("Friends"));
    public SelenideElement outcomeInvitationsBtn = peopleTabs.$$("button")
            .findBy(text("Outcome invitations"));
    public SelenideElement incomeInvitationsBtn = peopleTabs.$$("button")
            .findBy(text("Income invitations"));
    public SelenideElement tabPanelFriends = $("#simple-tabpanel-friends");

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
    public void checkUserIsPresentInPeopleList(@Nonnull String username) {
        peopleList.findBy(text(username))
                .shouldBe(visible);
    }

    @Step("Проверить, что пользователь '{username}' отсутствует в списке")
    public void checkUserIsNotPresentInPeopleList(@Nonnull String username) {
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
    public PeoplePage clickAddButton(@Nonnull String username) {
        getRow(username).$$("button").findBy(text("Add")).click();
        return this;
    }

    @Step("Нажать кнопку 'Принять' для пользователя '{username}'")
    @Nonnull
    public PeoplePage clickAcceptButton(@Nonnull String username) {
        getRow(username).$$("button").findBy(text("Accept")).click();
        return this;
    }

    @Step("Нажать кнопку 'Отклонить' для пользователя '{username}'")
    @Nonnull
    public PeoplePage clickDeclineButton(@Nonnull String username) {
        getRow(username).$$("button").findBy(text("Decline")).click();
        return this;
    }

    @Nonnull
    private SelenideElement getRow(@Nonnull String username) {
        return peopleList.findBy(text(username));
    }
}
