package io.student.rangiffler.model.graphql;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record GraphQlRequest(String query,
                             Map<String, Object> variables) {

    public GraphQlRequest(String query) {
        this(query, null);
    }
}