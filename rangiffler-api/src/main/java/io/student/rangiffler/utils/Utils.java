package io.student.rangiffler.utils;

import lombok.experimental.UtilityClass;

import java.util.Base64;

@UtilityClass
public class Utils {

    public byte[] decodeDataUriBase64(String src) {
        if (src == null || src.isBlank()) {
            return null;
        }

        // Поддержка формата: data:image/png;base64,AAAA...
        int comma = src.indexOf(',');
        var base64 = (comma >= 0) ? src.substring(comma + 1) : src;

        return Base64.getDecoder().decode(base64);
    }
}
