package io.student.rangiffler.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest;
import io.qameta.allure.Step;
import io.student.rangiffler.exceptions.BrokenTestException;
import io.student.rangiffler.model.client.CountriesGraphQLQuery;
import io.student.rangiffler.model.client.CountriesProjectionRoot;
import io.student.rangiffler.model.client.UserGraphQLQuery;
import io.student.rangiffler.model.client.UserProjectionRoot;
import io.student.rangiffler.model.graphql.GraphQlRequest;
import io.student.rangiffler.model.graphql.GraphQlResponse;
import io.student.rangiffler.model.types.Country;
import io.student.rangiffler.model.types.User;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InternalApiClient extends RestClient {

    private static final ObjectMapper OM = new ObjectMapper().findAndRegisterModules();

    private static final String FRIENDS_QUERY = """
            query($page: Int, $size: Int) {
              user {
                friends(page: $page, size: $size) {
                  edges { node { id username firstname surname avatar friendStatus } }
                  pageInfo { hasNextPage hasPreviousPage }
                }
              }
            }
            """;

    private final InternalApi internalApi;

    public InternalApiClient() {
        super(CFG.apiUrl());
        this.internalApi = retrofit.create(InternalApi.class);
    }

    @Step("GraphQL: получить список стран")
    public List<Country> countries(String token) {
        GraphQLQueryRequest request = new GraphQLQueryRequest(
                CountriesGraphQLQuery.newRequest().build(),
                new CountriesProjectionRoot<>().code().name().flag()
        );
        return dataAsList(token, request, Country.class);
    }

    @Step("GraphQL: получить текущего пользователя")
    public User currentUser(String token) {
        var projection = new UserProjectionRoot<>();
        projection.id().username().firstname().surname().avatar();
        projection.location().code().name().flag();

        GraphQLQueryRequest request = new GraphQLQueryRequest(
                UserGraphQLQuery.newRequest().build(),
                projection
        );
        return data(token, request, User.class);
    }

    @Step("GraphQL: получить друзей текущего пользователя")
    public List<User> friends(String token, int page, int size) {
        GraphQlResponse response = execute(token,
                new GraphQlRequest(FRIENDS_QUERY, Map.of("page", page, "size", size)));

        List<User> friends = new ArrayList<>();
        for (JsonNode edge : requireData(response).path("user").path("friends").path("edges")) {
            friends.add(OM.convertValue(edge.get("node"), User.class));
        }
        return friends;
    }

    public GraphQlResponse execute(String token, GraphQLQueryRequest request) {
        return execute(token, new GraphQlRequest(request.serialize()));
    }

    @Step("GraphQL: выполнить запрос")
    public GraphQlResponse execute(String token, GraphQlRequest request) {
        try {
            Response<GraphQlResponse> response = internalApi.graphql(bearer(token), request).execute();

            if (!response.isSuccessful() || response.body() == null) {
                throw new BrokenTestException("GraphQL request failed with HTTP %d".formatted(response.code()));
            }
            return response.body();
        } catch (IOException e) {
            throw new BrokenTestException("Can`t execute GraphQL request: " + e.getMessage());
        }
    }

    private <T> T data(String token, GraphQLQueryRequest request, Class<T> type) {
        return OM.convertValue(payload(token, request), type);
    }

    private <T> List<T> dataAsList(String token, GraphQLQueryRequest request, Class<T> type) {
        return OM.convertValue(
                payload(token, request),
                OM.getTypeFactory().constructCollectionType(List.class, type)
        );
    }

    private JsonNode requireData(GraphQlResponse response) {
        if (response.hasErrors()) {
            throw new BrokenTestException("GraphQL returned errors: " + response.errorMessages());
        }
        if (response.data() == null) {
            throw new BrokenTestException("GraphQL response has no data");
        }
        return response.data();
    }

    private JsonNode payload(String token, GraphQLQueryRequest request) {
        String operation = request.getQuery().getOperationName();
        JsonNode payload = requireData(execute(token, request)).get(operation);
        if (payload == null) {
            throw new BrokenTestException("No data for operation '%s' in GraphQL response".formatted(operation));
        }
        return payload;
    }

    private String bearer(String token) {
        return token.startsWith("Bearer ") ? token : "Bearer " + token;
    }
}