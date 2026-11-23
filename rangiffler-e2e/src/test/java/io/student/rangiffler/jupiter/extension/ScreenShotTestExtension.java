package io.student.rangiffler.jupiter.extension;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Allure;
import io.student.rangiffler.jupiter.annotation.ScreenShotTest;
import io.student.rangiffler.model.allure.ScreenDiff;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

public class ScreenShotTestExtension implements ParameterResolver, TestExecutionExceptionHandler {
    public static final String REWRITE_EXPECTED_PROPERTY = "rewriteExpected";
    public static final String RESOURCES_DIR_PROPERTY = "screenshots.resourcesDir";
    private static final String EXPECTED_KEY = "expected";
    private static final String ACTUAL_KEY = "actual";
    private static final String DIFF_KEY = "diff";
    private static final String PNG_FORMAT = "png";
    private static final String PNG_DATA_URI_PREFIX = "data:image/png;base64,";
    private static final Path DEFAULT_RESOURCES_DIR = Path.of("src/test/resources");

    public static final ExtensionContext.Namespace NAMESPACE =
            ExtensionContext.Namespace.create(ScreenShotTestExtension.class);
    public static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public boolean supportsParameter(@Nonnull ParameterContext parameterContext, @Nonnull ExtensionContext extensionContext) throws ParameterResolutionException {
        return AnnotationSupport.isAnnotated(extensionContext.getRequiredTestMethod(), ScreenShotTest.class) &&
                parameterContext.getParameter().getType() == BufferedImage.class;
    }

    @Override
    @Nonnull
    public Object resolveParameter(@Nonnull ParameterContext parameterContext, @Nonnull ExtensionContext extensionContext) throws ParameterResolutionException {
        Path path = expectedPath(extensionContext);
        try (var input = Files.newInputStream(path)) {
            BufferedImage expected = ImageIO.read(input);
            if (expected == null) {
                throw new IOException("Unsupported image format");
            }
            return expected;
        } catch (IOException e) {
            throw new ParameterResolutionException("Cannot read expected screenshot: " + path, e);
        }
    }

    @Override
    public void handleTestExecutionException(@Nonnull ExtensionContext context, @Nonnull Throwable throwable) throws Throwable {
        BufferedImage expected = getExpected();
        BufferedImage actual = getActual();
        BufferedImage diff = getDiff();
        if (expected != null && actual != null && diff != null) {
            try {
                ScreenDiff screenDiff = new ScreenDiff(
                        PNG_DATA_URI_PREFIX + Base64.getEncoder().encodeToString(imageToBytes(expected)),
                        PNG_DATA_URI_PREFIX + Base64.getEncoder().encodeToString(imageToBytes(actual)),
                        PNG_DATA_URI_PREFIX + Base64.getEncoder().encodeToString(imageToBytes(diff)));
                Allure.addAttachment("Screenshot diff",
                        "application/vnd.allure.image.diff",
                        MAPPER.writeValueAsString(screenDiff));
            } catch (Exception attachmentError) {
                throwable.addSuppressed(attachmentError);
            }
        }
        throw throwable;
    }

    public static void rewriteExpected(@Nonnull BufferedImage actual) {
        Path path = expectedPath(TestMethodContextExtension.context());
        try (var output = Files.newOutputStream(path)) {
            ImageIO.write(actual, PNG_FORMAT, output);
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot rewrite expected screenshot: " + path, e);
        }
    }

    private static Path expectedPath(ExtensionContext context) {
        String resource = AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), ScreenShotTest.class)
                .orElseThrow(() -> new ExtensionConfigurationException("@ScreenShotTest is required"))
                .value();
        String configuredResources = System.getProperty(RESOURCES_DIR_PROPERTY);
        Path resources = configuredResources != null
                ? Path.of(configuredResources)
                : Files.isDirectory(DEFAULT_RESOURCES_DIR)
                        ? DEFAULT_RESOURCES_DIR : Path.of("rangiffler-e2e/src/test/resources");
        resources = resources.toAbsolutePath().normalize();
        Path expected = resources.resolve(resource).normalize();
        if (!expected.startsWith(resources)) {
            throw new ExtensionConfigurationException("Expected screenshot must be inside " + resources);
        }
        return expected;
    }

    public static void setExpected(@Nonnull BufferedImage expected) {
        TestMethodContextExtension.context().getStore(NAMESPACE).put(EXPECTED_KEY, expected);
    }

    @Nullable
    public static BufferedImage getExpected() {
        return TestMethodContextExtension.context().getStore(NAMESPACE).get(EXPECTED_KEY, BufferedImage.class);
    }

    public static void setActual(@Nonnull BufferedImage actual) {
        TestMethodContextExtension.context().getStore(NAMESPACE).put(ACTUAL_KEY, actual);
    }

    @Nullable
    public static BufferedImage getActual() {
        return TestMethodContextExtension.context().getStore(NAMESPACE).get(ACTUAL_KEY, BufferedImage.class);
    }

    public static void setDiff(@Nonnull BufferedImage diff) {
        TestMethodContextExtension.context().getStore(NAMESPACE).put(DIFF_KEY, diff);
    }

    @Nullable
    public static BufferedImage getDiff() {
        return TestMethodContextExtension.context().getStore(NAMESPACE).get(DIFF_KEY, BufferedImage.class);
    }

    @Nonnull
    private static byte[] imageToBytes(@Nonnull BufferedImage image) {
        try (var outputStream = new ByteArrayOutputStream()) {
            ImageIO.write(image, PNG_FORMAT, outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
