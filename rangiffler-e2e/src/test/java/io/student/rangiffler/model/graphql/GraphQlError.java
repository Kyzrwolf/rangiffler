package io.student.rangiffler.model.graphql;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GraphQlError(String message,
                           List<Object> path,
                           JsonNode extensions) {

    public String classification() {
        return extensions == null || extensions.get("classification") == null
                ? null
                : extensions.get("classification").asText();
    }
}
