package io.student.rangiffler.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record UserJson(
    UUID id,
    String username,
    String password,
    String firstname,
    String surname,
    String avatar
) {}
