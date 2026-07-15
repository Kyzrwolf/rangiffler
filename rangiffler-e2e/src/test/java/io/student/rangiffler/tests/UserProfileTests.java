package io.student.rangiffler.tests;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Allure;
import io.student.rangiffler.config.Config;
import io.student.rangiffler.jupiter.annotation.ScreenShotTest;
import io.student.rangiffler.jupiter.annotation.UserType;
import io.student.rangiffler.jupiter.extension.UserExtension;
import io.student.rangiffler.page.LoginPage;
import io.student.rangiffler.service.UserDbClient;
import net.datafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;

import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static com.codeborne.selenide.Selenide.$;
import static io.student.rangiffler.jupiter.annotation.UserType.Type.EMPTY;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(UserExtension.class)
public class UserProfileTests extends BaseTest {

    private static final Config CFG = Config.getInstance();
    private Faker faker = new Faker();
    private final UserDbClient usersClient = new UserDbClient();

    @ScreenShotTest("img/expected_avatar.png")
    @DisplayName("Редактирование профиля пользователя")
    public void changeUserProfile(@UserType(EMPTY) @Nonnull UserExtension.TestUser user, @Nonnull BufferedImage expected) throws IOException {
        var firstName = faker.name().firstName();
        var surname = faker.name().lastName();
        var location = usersClient.getRandomCountryName();
        var profilePage = Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickLoginBtn()
                .login(user.username(), user.password())
                .clickProfileBtn()
                .setFirstName(firstName)
                .setSurname(surname)
                .setLocation(location)
                .uploadNewAvatar("avatar.jpg")
                .clickSaveBtn();
        Selenide.refresh();

        profilePage.checkFirstName(firstName)
                .checkSurname(surname)
                .checkLocation(location);

        var actualAvatar = ImageIO.read($(".MuiAvatar-img").screenshot());
        var imageDiff = new ImageDiffer().makeDiff(expected, actualAvatar);
        var resultImage = imageDiff.getMarkedImage();
        Allure.addAttachment(
                "diff image",
                "image/png",
                new ByteArrayInputStream(imageToBytes(resultImage)),
                "png"
        );

        assertFalse(imageDiff.hasDiff(), "Avatar image is different");
    }

        @Nonnull
        private static byte[] imageToBytes(@Nonnull BufferedImage image) {
            try (var outputStream = new ByteArrayOutputStream()) {
                ImageIO.write(image, "png", outputStream);
                return outputStream.toByteArray();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
}
