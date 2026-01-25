package io.student.rangiffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.CollectionCondition.empty;
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

    public ElementsCollection peopleList = $$("tr");

    public PeoplePage clickFriendsBtn() {
        friendsBtn.click();
        return this;
    }

    public PeoplePage clickOutcomeInvitationsBtn() {
        outcomeInvitationsBtn.click();
        return this;
    }

    public PeoplePage clickIncomeInvitationsBtn() {
        incomeInvitationsBtn.click();
        return this;
    }

    public void checkUserIsPresentInPeopleList(String username) {
        peopleList.findBy(text(username))
                .shouldBe(visible);
    }

    public void checkPeopleListIsEmpty() {
        peopleList.shouldBe(empty);
    }
}
