package io.student.rangiffler.model.allure;

import javax.annotation.Nullable;

public record ScreenDiff(@Nullable String expected, @Nullable String actual, @Nullable String diff) {
}
