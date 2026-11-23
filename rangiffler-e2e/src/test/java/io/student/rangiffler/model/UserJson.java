package io.student.rangiffler.model;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public record UserJson(
    @Nonnull UUID id,
    @Nullable UUID udId,
    @Nonnull String username,
    @Nonnull String password,
    @Nullable String firstname,
    @Nullable String surname,
    @Nullable String avatar
) {}
