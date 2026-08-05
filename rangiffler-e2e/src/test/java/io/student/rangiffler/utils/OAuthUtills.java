package io.student.rangiffler.utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import javax.annotation.ParametersAreNonnullByDefault;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

@UtilityClass
@ParametersAreNonnullByDefault
public class OAuthUtills {

    private static final SecureRandom secureRandom = new SecureRandom();

    public String generateCodeVerifier() {
        var code = new byte[32];
        secureRandom.nextBytes(code);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(code);
    }

    @SneakyThrows
    public String generateCodeChallenge(String codeVerifier) {
        var bytes = codeVerifier.getBytes(StandardCharsets.US_ASCII);
        var md = MessageDigest.getInstance("SHA-256");
        md.update(bytes);
        var digest = md.digest();
        return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
    }
}
