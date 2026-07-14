package io.student.rangiffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

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

    @Nonnull
    public PeoplePage clickFriendsBtn() {
        friendsBtn.click();
        return this;
    }

    @Nonnull
    public PeoplePage clickAllBtn() {
        peopleTabs.$$("button").findBy(text("All")).click();
        return this;
    }

    @Nonnull
    public PeoplePage clickOutcomeInvitationsBtn() {
        outcomeInvitationsBtn.click();
        return this;
    }

    @Nonnull
    public PeoplePage clickIncomeInvitationsBtn() {
        incomeInvitationsBtn.click();
        return this;
    }

    public void checkUserIsPresentInPeopleList(@Nonnull String username) {
        peopleList.findBy(text(username))
                .shouldBe(visible);
    }

    public void checkUserIsNotPresentInPeopleList(@Nonnull String username) {
        peopleList.findBy(text(username))
                .shouldNotBe(visible);
    }

    public void checkPeopleListIsEmpty() {
        tabPanelFriends.shouldHave(text("There are no users yet"));
        peopleList.shouldHave(size(1));
    }

    @Nonnull
    public PeoplePage clickAddButton(@Nonnull String username) {
        getRow(username).$$("button").findBy(text("Add")).click();
        return this;
    }

    @Nonnull
    public PeoplePage clickAcceptButton(@Nonnull String username) {
        getRow(username).$$("button").findBy(text("Accept")).click();
        return this;
    }

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
