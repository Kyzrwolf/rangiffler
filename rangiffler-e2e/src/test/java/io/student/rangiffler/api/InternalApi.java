package io.student.rangiffler.api;

import io.student.rangiffler.model.graphql.GraphQlRequest;
import io.student.rangiffler.model.graphql.GraphQlResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface InternalApi {

    @POST("graphql")
    Call<GraphQlResponse> graphql(@Header("Authorization") String bearerToken,
                                  @Body GraphQlRequest request);
}