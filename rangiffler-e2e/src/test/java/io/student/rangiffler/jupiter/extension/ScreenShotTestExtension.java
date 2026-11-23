package io.student.rangiffler.jupiter.extension;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Allure;
import io.student.rangiffler.jupiter.annotation.ScreenShotTest;
import io.student.rangiffler.model.allure.ScreenDiff;
import lombok.SneakyThrows;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class ScreenShotTestExtension implements ParameterResolver, TestExecutionExceptionHandler {

    public static final ExtensionContext.Namespace NAMESPACE =
            ExtensionContext.Namespace.create(ScreenShotTestExtension.class);
    public static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public boolean supportsParameter(@Nonnull ParameterContext parameterContext, @Nonnull ExtensionContext extensionContext) throws ParameterResolutionException {
        return AnnotationSupport.isAnnotated(extensionContext.getRequiredTestMethod(), ScreenShotTest.class) &&
        parameterContext.getParameter().getType().isAssignableFrom(BufferedImage.class);
    }

    @SneakyThrows
    @Override
    @Nonnull
    public Object resolveParameter(@Nonnull ParameterContext parameterContext, @Nonnull ExtensionContext extensionContext) throws ParameterResolutionException {
        return ImageIO.read(new ClassPathResource("img/expected_avatar.png").getInputStream());    }

    @Override
    public void handleTestExecutionException(@Nonnull ExtensionContext context, @Nonnull Throwable throwable) throws Throwable {
        ScreenDiff screenDiff = new ScreenDiff(
                "data:image/png;base64," + Base64.getEncoder().encodeToString(imageToBytes(getExpected())),
                "data:image/png;base64," + Base64.getEncoder().encodeToString(imageToBytes(getActual())),
                "data:image/png;base64," + Base64.getEncoder().encodeToString(imageToBytes(getDiff())));

        Allure.addAttachment("Screenshot diff",
                "application/vnd.allure.image.diff",
                MAPPER.writeValueAsString(screenDiff));
        throw throwable;
    }

    public static void setExpected(@Nonnull BufferedImage expected) {
        TestMethodContextExtension.context().getStore(NAMESPACE).put("expected", expected);
    }

    @Nullable
    public static BufferedImage getExpected() {
        return TestMethodContextExtension.context().getStore(NAMESPACE).get("expected", BufferedImage.class) ;
    }

    public static void setActual(@Nonnull BufferedImage actual) {
        TestMethodContextExtension.context().getStore(NAMESPACE).put("actual", actual);
    }

    @Nullable
    public static BufferedImage getActual() {
        return TestMethodContextExtension.context().getStore(NAMESPACE).get("actual", BufferedImage.class) ;
    }

    public static void setDiff(@Nonnull BufferedImage diff) {
        TestMethodContextExtension.context().getStore(NAMESPACE).put("diff", diff);
    }

    @Nullable
    public static BufferedImage getDiff() {
        return TestMethodContextExtension.context().getStore(NAMESPACE).get("diff", BufferedImage.class) ;
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
