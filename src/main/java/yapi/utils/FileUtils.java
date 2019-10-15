package yapi.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class FileUtils {

    private FileUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static String getSuffix(String file) {
        return file.substring(file.lastIndexOf('.') + 1);
    }

    public static String getSuffix(File file) {
        return file.getName().substring(file.getName().lastIndexOf('.') + 1);
    }

    public static String getName(String file) {
        if (!file.contains(".")) {
            return file;
        }
        return file.substring(0, file.lastIndexOf('.'));
    }

    public static String getName(File file) {
        if (!file.getName().contains(".")) {
            return file.getName();
        }
        return file.getName().substring(0, file.getName().lastIndexOf('.'));
    }

    public static byte[] checkSum(File file) {
        String text = "";
        try (Scanner scanner = new Scanner(file)) {
            text = scanner.useDelimiter("\\A").next();
        } catch (FileNotFoundException e) {
            return new byte[0];
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            return digest.digest((text + "").getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            return new byte[0];
        }
    }

}
