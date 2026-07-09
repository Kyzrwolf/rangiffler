package io.student.rangiffler.model;

import java.util.UUID;

public record UserJson(
    UUID id,
    UUID udId,
    String username,
    String password,
    String firstname,
    String surname,
    String avatar
) {}
