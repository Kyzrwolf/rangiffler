package io.student.rangiffler.utils;

import io.student.rangiffler.jupiter.extension.ScreenShotTestExtension;
import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;

import java.awt.image.BufferedImage;
import java.util.function.BooleanSupplier;

import static io.student.rangiffler.jupiter.extension.ScreenShotTestExtension.REWRITE_EXPECTED_PROPERTY;

public class ScreenDiffResult implements BooleanSupplier {
    private final BufferedImage expected;
    private final BufferedImage actual;
    private final ImageDiff diff;
    private final boolean hasDiff;

    public ScreenDiffResult(BufferedImage expected, BufferedImage actual) {
        this.expected = expected;
        this.actual = actual;
        this.diff = new ImageDiffer().makeDiff(expected, actual);
        this.hasDiff = diff.hasDiff();
    }

    @Override
    public boolean getAsBoolean() {
        if (Boolean.getBoolean(REWRITE_EXPECTED_PROPERTY)) {
            ScreenShotTestExtension.rewriteExpected(actual);
            return false;
        }
        if (hasDiff) {
            ScreenShotTestExtension.setExpected(expected);
            ScreenShotTestExtension.setActual(actual);
            ScreenShotTestExtension.setDiff(diff.getMarkedImage());
        }
        return hasDiff;
    }
}
