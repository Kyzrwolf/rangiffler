package io.student.rangiffler.tests;

import com.codeborne.selenide.Selenide;
import io.student.rangiffler.config.Config;
import io.student.rangiffler.jupiter.annotation.UserType;
import io.student.rangiffler.jupiter.extension.UsersQueueExtension;
import io.student.rangiffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.student.rangiffler.jupiter.annotation.UserType.Type.*;

@ExtendWith(UsersQueueExtension.class)
public class FriendsWebTests extends BaseTest {

    private static final Config CFG = Config.getInstance();

    @Test
    public void friendShouldBePresentInFriendsTable(@UserType(WITH_FRIEND) UsersQueueExtension.StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickLoginBtn()
                .login(user.username(), user.password())
                .checkTravelPageIsOpen()
                .clickPeopleBtn()
                .clickFriendsBtn()
                .checkUserIsPresentInPeopleList(user.friend().username());

    }

    @Test
//    @DisabledByIssue("3")
    public void friendsTableShouldBeEmptyForNewUser(@UserType(EMPTY) UsersQueueExtension.StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickLoginBtn()
                .login(user.username(), user.password())
                .checkTravelPageIsOpen()
                .clickPeopleBtn()
                .checkPeopleListIsEmpty();
    }

    @Test
    public void incomeInvitationBePresentInFriendsTable(@UserType(WITH_INCOME_REQUEST) UsersQueueExtension.StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickLoginBtn()
                .login(user.username(), user.password())
                .checkTravelPageIsOpen()
                .clickPeopleBtn()
                .clickIncomeInvitationsBtn()
                .checkUserIsPresentInPeopleList(user.friend().username());

    }

    @Test
    public void outcomeInvitationBePresentInAllPeoplesTable(@UserType(WITH_OUTCOME_REQUEST) UsersQueueExtension.StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickLoginBtn()
                .login(user.username(), user.password())
                .checkTravelPageIsOpen()
                .clickPeopleBtn()
                .clickOutcomeInvitationsBtn()
                .checkUserIsPresentInPeopleList(user.friend().username());

    }
}

