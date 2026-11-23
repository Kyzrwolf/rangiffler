package io.student.rangiffler.jupiter.annotation;

import io.student.rangiffler.jupiter.extension.ScreenShotTestExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.annotation.Nonnull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Test
@ExtendWith(ScreenShotTestExtension.class)
public @interface ScreenShotTest {
    @Nonnull
    String value();
}
