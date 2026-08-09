package io.student.rangiffler.api;

import com.fasterxml.jackson.databind.JsonNode;
import io.qameta.allure.Step;
import lombok.SneakyThrows;
import retrofit2.Response;

import javax.annotation.Nonnull;

public class GithubApiClient extends RestClient {

    private static final String GH_TOKEN_ENV = "GITHUB_TOKEN";
    private final GithubApi githubApi;

    public GithubApiClient() {
        super(CFG.githubUrl());
        githubApi = retrofit.create(GithubApi.class);
    }

    @Step("Получить статус issue '{issueNumber}' на GitHub")
    @SneakyThrows
    @Nonnull
    public String issueState(@Nonnull String issueNumber) {
        String token = System.getenv(GH_TOKEN_ENV);
        if (token == null || token.isBlank()) {
            throw new IllegalStateException("Environment variable '" + GH_TOKEN_ENV + "' is not set");
        }
        Response<JsonNode> response = githubApi.issue("Bearer " + System.getenv(GH_TOKEN_ENV), issueNumber).execute();

        if (response.body() == null || response.body().get("state") == null) {
            throw new RuntimeException("Invalid GitHub response: missing 'state'");
        }

        if (response.isSuccessful()) {
            return response.body().get("state").asText();
        }
        throw new RuntimeException("Failed to fetch issue state from GitHub. Status code: " + response.code());
    }
}
