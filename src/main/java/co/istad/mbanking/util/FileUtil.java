package co.istad.mbanking.util;

import java.util.UUID;

public class FileUtil {

    public static String generateFileName(String originalFileName) {

        String newName = UUID.randomUUID().toString();
        String extension = extractExtension(originalFileName);

        return String.format("%s.%s", newName, extension);
    }

    public static String extractExtension(String originalFileName) {
        int lastDotIndex = originalFileName.lastIndexOf(".");
        return originalFileName.substring(lastDotIndex + 1);
    }

}
