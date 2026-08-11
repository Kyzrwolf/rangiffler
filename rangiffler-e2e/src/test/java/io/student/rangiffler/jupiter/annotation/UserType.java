package io.student.rangiffler.jupiter.annotation;

import io.student.rangiffler.jupiter.extension.UserExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.annotation.Nonnull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(UserExtension.class)
public @interface UserType {
    @Nonnull
    Type value() default Type.EMPTY;

    enum Type {
        EMPTY,
        WITH_FRIEND,
        WITH_INCOME_REQUEST,
        WITH_OUTCOME_REQUEST,
        STRANGER
    }
}
