package io.student.rangiffler.api.core;

import io.student.rangiffler.jupiter.extension.ApiLoginExtension;
import okhttp3.Interceptor;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.util.Objects;

public class CodeInterceptor implements Interceptor {
    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        final Response response = chain.proceed(chain.request());
        if (response.isRedirect()) {
            var location = Objects.requireNonNull(
                    response.header("Location")
                );
            if (location.contains("code=")) {
                ApiLoginExtension.setCode(
                        StringUtils.substringAfter(location, "code=")
                );
            }
        }
        return response;
    }
}
