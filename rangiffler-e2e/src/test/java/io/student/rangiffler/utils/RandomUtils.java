package io.student.rangiffler.utils;

import lombok.experimental.UtilityClass;

import javax.annotation.Nonnull;
import java.util.Random;

@UtilityClass
public class RandomUtils {

    Random random = new Random();

    /**
     * Generates a random alphanumeric string of the specified length.
     * <p>
     * Uses ASCII ranges: digits {@code 0-9} (48–57), uppercase {@code A-Z} (65–90),
     * lowercase {@code a-z} (97–122). Characters outside these ranges (e.g. {@code :;<=>?@})
     * are filtered out.
     *
     * @param length the number of characters in the resulting string
     * @return a random alphanumeric string of the given length
     */
    @Nonnull
    public String generateRandomAlphanumericString(int length) {
        var leftLimit = 48;
        var rightLimit = 122;
        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
