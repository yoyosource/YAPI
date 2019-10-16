package yapi.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
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

    public static String getUserHome() {
        return System.getProperty("user.home");
    }

    public static String getWorkingDirectory() {
        return FileUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    }

    public static String removeFileFromPath(String s) {
        if (!s.contains(".")) {
            return s;
        }
        return s.substring(0, s.lastIndexOf('/'));
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

    public static byte[] fileContentAsBytes(File file) {
        try (InputStream inputStream = new FileInputStream(file)) {
            return inputStream.readAllBytes();
        } catch (IOException e) {

        }
        return new byte[0];
    }

    public static String[] fileContentAsString(File file) {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            List<String> strings = new ArrayList<>();
            String s = "";
            while ((s = bufferedReader.readLine()) != null) {
                strings.add(s);
            }
            return strings.toArray(new String[0]);
        } catch (IOException e) {

        }
        return new String[0];
    }

}
