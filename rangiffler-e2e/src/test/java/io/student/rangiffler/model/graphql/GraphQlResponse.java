package io.student.rangiffler.model.graphql;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public record GraphQlResponse(JsonNode data,
                              List<GraphQlError> errors) {

    public boolean hasErrors() {
        return errors != null && !errors.isEmpty();
    }

    public String errorMessages() {
        return errors == null
                ? ""
                : errors.stream().map(GraphQlError::message).toList().toString();
    }
}