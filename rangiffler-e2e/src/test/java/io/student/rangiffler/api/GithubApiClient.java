package io.student.rangiffler.api;

import com.fasterxml.jackson.databind.JsonNode;
import io.student.rangiffler.config.Config;
import lombok.SneakyThrows;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class GithubApiClient {

    private static final String GH_TOKEN_ENV = "GITHUB_TOKEN";

    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Config.getInstance().githubUrl())
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final GithubApi githubApi = retrofit.create(GithubApi.class);

    @SneakyThrows
    public String issueState(String issueNumber) {
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
