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

public class ScreenShotTestExtension implements ParameterResolver, TestExecutionExceptionHandler {

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
                null,
                null,
                null
        );

        Allure.addAttachment("Screenshot diff",
                "application/vnd.allure.image.diff",
                MAPPER.writeValueAsString(screenDiff));
        throw throwable;
    }

    public static void setExpected(@Nonnull BufferedImage expected) {
        //TODO your code may be here
    }

    @Nullable
    public static BufferedImage getExpected() {
        return null;
    }
}
