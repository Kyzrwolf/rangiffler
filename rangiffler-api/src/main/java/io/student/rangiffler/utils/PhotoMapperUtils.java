package io.student.rangiffler.utils;

import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class PhotoMapperUtils {

    public static final String DATA_IMAGE_PNG_BASE_64 = "data:image/png;base64,";
    public static final String DATA_IMAGE_JPEG_BASE_64 = "data:image/jpeg;base64,";

    public String getJpegImage(byte[] imageBytes) {
        return DATA_IMAGE_JPEG_BASE_64 + Base64.getEncoder().encodeToString(imageBytes);
    }

    public String getPngImage(byte[] imageBytes) {
        return DATA_IMAGE_PNG_BASE_64 + Base64.getEncoder().encodeToString(imageBytes);
    }

}
