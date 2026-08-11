package io.student.rangiffler.jupiter.annotation;

import io.student.rangiffler.jupiter.extension.ApiLoginExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(ApiLoginExtension.class)
@Target(ElementType.METHOD)
public @interface ApiLogin {
    String username() default "";
    String password() default "";

    boolean setupBrowser() default true;
}
