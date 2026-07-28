package io.student.rangiffler.tests;

import com.codeborne.selenide.Selenide;
import io.student.rangiffler.config.Config;
import io.student.rangiffler.jupiter.annotation.UserType;
import io.student.rangiffler.jupiter.extension.UserExtension;
import io.student.rangiffler.page.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.annotation.Nonnull;

import static io.student.rangiffler.jupiter.annotation.UserType.Type.*;

@ExtendWith(UserExtension.class)
public class FriendsWebTests extends BaseTest {

    private static final Config CFG = Config.getInstance();

    @Test
    @DisplayName("Друг присутствует в списке друзей у пользователя с друзьями")
    public void friendShouldBePresentInFriendsTable(@UserType(WITH_FRIEND) @Nonnull UserExtension.TestUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickLoginBtn()
                .login(user.username(), user.password())
                .checkTravelPageIsOpen()
                .clickPeopleBtn()
                .clickFriendsBtn()
                .checkUserIsPresentInPeopleList(user.friend().username());
    }

    @Test
    @DisplayName("Список друзей пуст у нового пользователя")
//    @DisabledByIssue("3")
    public void friendsTableShouldBeEmptyForNewUser(@UserType(EMPTY) @Nonnull UserExtension.TestUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickLoginBtn()
                .login(user.username(), user.password())
                .checkTravelPageIsOpen()
                .clickPeopleBtn()
                .checkPeopleListIsEmpty();
    }

    @Test
    @DisplayName("Исходящая заявка в друзья отображается в списке заявок")
    public void incomeInvitationBePresentInFriendsTable(@UserType(WITH_INCOME_REQUEST) @Nonnull UserExtension.TestUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickLoginBtn()
                .login(user.username(), user.password())
                .checkTravelPageIsOpen()
                .clickPeopleBtn()
                .clickIncomeInvitationsBtn()
                .checkUserIsPresentInPeopleList(user.friend().username());
    }

    @Test
    @DisplayName("Входящая заявка в друзья отображается в списке заявок")
    public void outcomeInvitationBePresentInAllPeoplesTable(@UserType(WITH_OUTCOME_REQUEST) @Nonnull UserExtension.TestUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickLoginBtn()
                .login(user.username(), user.password())
                .checkTravelPageIsOpen()
                .clickPeopleBtn()
                .clickOutcomeInvitationsBtn()
                .checkUserIsPresentInPeopleList(user.friend().username());
    }

    @Test
    @DisplayName("Прием заявки в друзья")
    public void acceptFriendRequest(@UserType(WITH_INCOME_REQUEST) @Nonnull UserExtension.TestUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickLoginBtn()
                .login(user.username(), user.password())
                .clickPeopleBtn()
                .clickIncomeInvitationsBtn()
                .clickAcceptButton(user.friend().username())
                .checkAlertMessage("Invitation accepted")
                .clickFriendsBtn()
                .checkUserIsPresentInPeopleList(user.friend().username());
    }

    @Test
    @DisplayName("Отправка заявки в друзья")
    public void sendFriendRequest(@UserType(STRANGER) @Nonnull UserExtension.TestUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickLoginBtn()
                .login(user.username(), user.password())
                .clickPeopleBtn()
                .clickAllBtn()
                .searchPerson(user.friend().username())
                .clickAddButton(user.friend().username())
                .checkAlertMessage("Invitation sent")
                .clickOutcomeInvitationsBtn()
                .checkUserIsPresentInPeopleList(user.friend().username());
    }

    @Test
    @DisplayName("Отклонение заявки в друзья")
    public void declineFriendRequest(@UserType(WITH_INCOME_REQUEST) @Nonnull UserExtension.TestUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickLoginBtn()
                .login(user.username(), user.password())
                .clickPeopleBtn()
                .clickIncomeInvitationsBtn()
                .clickDeclineButton(user.friend().username())
                .checkAlertMessage("Invitation declined")
                .clickFriendsBtn()
                .checkUserIsNotPresentInPeopleList(user.friend().username());
    }

    @Test
    @DisplayName("Удаление пользователя из друзей")
    public void removeFriend(@UserType(WITH_FRIEND) @Nonnull UserExtension.TestUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickLoginBtn()
                .login(user.username(), user.password())
                .clickPeopleBtn()
                .clickFriendsBtn()
                .removeUserFromFriends(user.friend().username())
                .checkAlertMessage("Friend deleted")
                .clickFriendsBtn()
                .checkUserIsNotPresentInPeopleList(user.friend().username());
    }


}

