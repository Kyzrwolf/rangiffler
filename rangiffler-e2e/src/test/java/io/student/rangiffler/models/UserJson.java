package io.student.rangiffler.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record UserJson(
    @JsonProperty("id")
    UUID id,
    @JsonProperty("username")
    String username,
    @JsonProperty("firstname")
    String firstname,
    @JsonProperty("surname")
    String surname,
    @JsonProperty("avatar")
    String avatar
) {}
