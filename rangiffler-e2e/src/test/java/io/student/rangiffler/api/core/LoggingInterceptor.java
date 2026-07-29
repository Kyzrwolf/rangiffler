package io.student.rangiffler.api.core;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.lang.NonNull;

import java.io.IOException;

@Slf4j
public class LoggingInterceptor implements Interceptor {

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        var request = chain.request();
        var requestLog = new StringBuilder();
        requestLog.append(request.method())
                .append(" ")
                .append(request.url())
                .append("\n")
                .append(request.headers());


        if (request.body() != null) {
            requestLog.append("Request body: \n")
                    .append(request.body());
        }

        log.info(requestLog.toString());

        var startTime = System.nanoTime();
        var response = chain.proceed(request);
        var tookMs = (System.nanoTime() - startTime) / 1_000_000;
        var bodyString = response.body() != null ? response.body().string() : "";
        var wrapped = ResponseBody.create(bodyString, response.body() != null ? response.body().contentType() : null);
        var responseLog = new StringBuilder();
        responseLog.append("Response: ")
                .append(response.code())
                .append(" ")
                .append(request.url())
                .append(" (")
                .append(tookMs)
                .append("ms)\n");
        responseLog.append(response.headers());
        responseLog.append("\nResponse body: \n")
                .append(bodyString);

        log.info(responseLog.toString());
        return response.newBuilder().body(wrapped).build();
    }
}
