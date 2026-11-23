package io.student.rangiffler.tests.web;

import com.codeborne.selenide.Selenide;
import io.student.rangiffler.jupiter.annotation.ScreenShotTest;
import io.student.rangiffler.jupiter.annotation.UserType;
import io.student.rangiffler.jupiter.extension.UserExtension;
import io.student.rangiffler.jupiter.extension.UserExtension.TestUser;
import io.student.rangiffler.page.LoginPage;
import io.student.rangiffler.page.TravelsMapPage;
import io.student.rangiffler.utils.ScreenDiffResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import static io.qameta.allure.Allure.step;
import static io.student.rangiffler.jupiter.annotation.UserType.Type.EMPTY;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(UserExtension.class)
public class TravelsMapTests extends BaseTest {
    private static final String PHOTO_DESCRIPTION = "A trip to France";
    private TravelsMapPage travelsPage;

    @AfterEach
    void deleteUploadedPhoto() {
        if (travelsPage != null) {
            // Reload also closes an unfinished dialog and reads photos saved before an assertion failed.
            Selenide.refresh();
            travelsPage.checkTravelPageIsOpen().deletePhotoIfPresent(PHOTO_DESCRIPTION);
        }
    }

    @ScreenShotTest("img/expected_travels_map.png")
    @DisplayName("Загруженное фото подсвечивает страну на карте")
    void uploadedPhotoShouldHighlightCountryOnMap(@UserType(EMPTY) TestUser user, BufferedImage expected) {
        travelsPage = Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickLoginBtn()
                .login(user.username(), user.password())
                .checkTravelPageIsOpen();

        travelsPage.clickAddPhoto()
                .uploadPhoto("avatar.jpg")
                .setCountry("fr")
                .setDescription(PHOTO_DESCRIPTION)
                .save()
                .checkAlertMessage("New post created");

        step("Проверить сохранённое фото и карту путешествий", () -> {
            travelsPage.checkPhotoPresent(PHOTO_DESCRIPTION, "France")
                    .prepareMapScreenshot();
            BufferedImage actual = ImageIO.read(travelsPage.worldMap.screenshot());
            assertFalse(new ScreenDiffResult(expected, actual), "Travels map is different");
        });
    }
}
