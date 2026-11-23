package io.student.rangiffler.tests.unit;

import io.student.rangiffler.jupiter.annotation.ScreenShotTest;
import io.student.rangiffler.jupiter.extension.ScreenShotTestExtension;
import io.student.rangiffler.jupiter.extension.TestMethodContextExtension;
import io.student.rangiffler.utils.ScreenDiffResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.api.parallel.Isolated;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static io.student.rangiffler.jupiter.extension.ScreenShotTestExtension.RESOURCES_DIR_PROPERTY;
import static io.student.rangiffler.jupiter.extension.ScreenShotTestExtension.REWRITE_EXPECTED_PROPERTY;

@Isolated("Changes screenshot system properties and uses temporary baselines")
public class ScreenShotTestExtensionTests {
    private static final String FIRST_BASELINE = "img/first.png";
    private static final String SECOND_BASELINE = "img/second.png";
    private static final String PNG_FORMAT = "png";
    private static final String REWRITE_ENABLED = "true";
    private static final String REWRITE_DISABLED = "false";
    @TempDir
    Path resources;

    private String previousResources;
    private String previousRewrite;

    @BeforeEach
    void createBaselines() throws IOException {
        previousResources = System.getProperty(RESOURCES_DIR_PROPERTY);
        previousRewrite = System.getProperty(REWRITE_EXPECTED_PROPERTY);
        System.setProperty(RESOURCES_DIR_PROPERTY, resources.toString());
        System.setProperty(REWRITE_EXPECTED_PROPERTY, REWRITE_DISABLED);
        Files.createDirectories(resources.resolve("img"));
        ImageIO.write(image(0xff123456), PNG_FORMAT, resources.resolve(FIRST_BASELINE).toFile());
        ImageIO.write(image(0xffabcdef), PNG_FORMAT, resources.resolve(SECOND_BASELINE).toFile());
    }

    @AfterEach
    void restoreProperties() {
        restore(RESOURCES_DIR_PROPERTY, previousResources);
        restore(REWRITE_EXPECTED_PROPERTY, previousRewrite);
    }

    @ScreenShotTest(SECOND_BASELINE)
    void loadsBaselineFromAnnotation(BufferedImage expected) {
        assertEquals(0xffabcdef, expected.getRGB(0, 0));
    }

    @ScreenShotTest(FIRST_BASELINE)
    void identicalImagesMatch(BufferedImage expected) {
        assertFalse(new ScreenDiffResult(expected, image(0xff123456)));
    }

    @ScreenShotTest(FIRST_BASELINE)
    void mismatchKeepsBaselineAndRecordsDiff(BufferedImage expected) throws IOException {
        byte[] original = Files.readAllBytes(resources.resolve(FIRST_BASELINE));
        assertTrue(new ScreenDiffResult(expected, image(0xfffedcba)));
        assertArrayEquals(original, Files.readAllBytes(resources.resolve(FIRST_BASELINE)));
        assertNotNull(ScreenShotTestExtension.getDiff());
    }

    @ScreenShotTest(SECOND_BASELINE)
    void rewriteReplacesOnlyAnnotatedSourceBaselineWithActual(BufferedImage expected) throws IOException {
        System.setProperty(REWRITE_EXPECTED_PROPERTY, REWRITE_ENABLED);
        BufferedImage actual = new BufferedImage(5, 3, BufferedImage.TYPE_INT_ARGB);
        actual.setRGB(4, 2, 0xfffedcba);
        byte[] otherBaseline = Files.readAllBytes(resources.resolve(FIRST_BASELINE));

        assertFalse(new ScreenDiffResult(expected, actual));

        BufferedImage rewritten = ImageIO.read(resources.resolve(SECOND_BASELINE).toFile());
        assertEquals(5, rewritten.getWidth());
        assertEquals(3, rewritten.getHeight());
        assertArrayEquals(actual.getRGB(0, 0, 5, 3, null, 0, 5),
                rewritten.getRGB(0, 0, 5, 3, null, 0, 5));
        assertArrayEquals(otherBaseline, Files.readAllBytes(resources.resolve(FIRST_BASELINE)));
    }

    @ScreenShotTest(FIRST_BASELINE)
    void rewriteFailureIsNotTreatedAsMatch(BufferedImage expected) throws IOException {
        System.setProperty(REWRITE_EXPECTED_PROPERTY, REWRITE_ENABLED);
        Files.delete(resources.resolve(FIRST_BASELINE));
        Files.createDirectory(resources.resolve(FIRST_BASELINE));

        assertThrows(UncheckedIOException.class,
                () -> new ScreenDiffResult(expected, image(0xfffedcba)).getAsBoolean());
    }

    @Test
    void failureBeforeImageComparisonPreservesOriginalException() {
        System.setProperty(REWRITE_EXPECTED_PROPERTY, REWRITE_ENABLED);
        AssertionError original = new AssertionError("Photo upload failed");
        Throwable thrown = assertThrows(Throwable.class,
                () -> new ScreenShotTestExtension().handleTestExecutionException(
                        TestMethodContextExtension.context(), original));
        assertSame(original, thrown);
    }

    private static BufferedImage image(int color) {
        BufferedImage image = new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 2; x++) {
                image.setRGB(x, y, color);
            }
        }
        return image;
    }

    private static void restore(String name, String previous) {
        if (previous == null) {
            System.clearProperty(name);
        } else {
            System.setProperty(name, previous);
        }
    }
}
