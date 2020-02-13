// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.file;

import yapi.exceptions.string.NoStringException;

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

    /**
     *
     * @since Version 1
     *
     * @param file
     * @return
     */
    public static String getSuffix(String file) {
        if (!file.contains(".")) {
            return "";
        }
        return file.substring(file.lastIndexOf('.') + 1);
    }

    /**
     *
     * @since Version 1
     *
     * @param file
     * @return
     */
    public static String getSuffix(File file) {
        if (!file.getName().contains(".")) {
            return "";
        }
        return file.getName().substring(file.getName().lastIndexOf('.') + 1);
    }

    /**
     *
     * @since Version 1
     *
     * @param file
     * @return
     */
    public static String getName(String file) {
        if (!file.contains(".")) {
            return file;
        }
        return file.substring(0, file.lastIndexOf('.'));
    }

    /**
     *
     * @since Version 1
     *
     * @param file
     * @return
     */
    public static String getName(File file) {
        if (!file.getName().contains(".")) {
            return file.getName();
        }
        return file.getName().substring(0, file.getName().lastIndexOf('.'));
    }

    /**
     * @since Version 1
     *
     * @return
     */
    public static String getUserHome() {
        return System.getProperty("user.home");
    }

    /**
     *
     * @since Version 1
     *
     * @return
     */
    public static String getWorkingDirectory() {
        return FileUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    }

    /**
     *
     * @since Version 1
     *
     * @param s
     * @return
     */
    public static String removeFileFromPath(String s) {
        if (!s.contains(".")) {
            return s;
        }
        return s.substring(0, s.lastIndexOf('/'));
    }

    /**
     *
     * @since Version 1
     *
     * @param file
     * @return
     */
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

    /**
     *
     * @since Version 1
     *
     * @param file
     * @return
     */
    public static byte[] fileContentAsBytes(File file) {
        try (InputStream inputStream = new FileInputStream(file)) {
            return inputStream.readAllBytes();
        } catch (IOException e) {
            return new byte[0];
        }
    }

    /**
     *
     * @since Version 1
     *
     * @param file
     * @return
     */
    public static String[] fileContentAsString(File file) {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            List<String> strings = new ArrayList<>();
            String s;
            while ((s = bufferedReader.readLine()) != null) {
                strings.add(s);
            }
            return strings.toArray(new String[0]);
        } catch (IOException e) {
            return new String[0];
        }
    }

    /**
     *
     * @since Version 1.1
     *
     * @param file
     * @return
     */
    public static long getSize(File file) {
        if (!file.isFile()) {
            return 0L;
        }
        return file.length();
    }

    /**
     *
     * @since Version 1.1
     *
     * @param file
     * @param round
     * @return
     */
    public static String getSize(File file, int round) {
        return getSize(file, true, false, round);
    }

    /**
     *
     * @since Version 1.1
     *
     * @param file
     * @param convert
     * @param round
     * @return
     */
    public static String getSize(File file, boolean convert, int round) {
        return getSize(file, convert, false, round);
    }

    /**
     *
     * @since Version 1.1
     *
     * @param file
     * @param convert
     * @return
     */
    public static String getSize(File file, boolean convert) {
        return getSize(file, convert, false, 0);
    }

    /**
     *
     * @since Version 1.1
     *
     * @param file
     * @param convert
     * @param floating
     * @return
     */
    public static String getSize(File file, boolean convert, boolean floating) {
        return getSize(file, convert, floating, 0);
    }

    /**
     *
     * @since Version 1.1
     *
     * @param file
     * @param convert
     * @param floating
     * @param round
     * @return
     */
    public static String getSize(File file, boolean convert, boolean floating, int round) {
        if (!convert) {
            return getSize(file) + "";
        }
        long size = getSize(file);
        int i = 0;
        while (size > 1024) {
            size /= 1024;
            i++;
        }
        char[] chars = new char[]{'b', 'K', 'M', 'G', 'T', 'P', 'Y'};
        if (!floating) {
            return size + "" + chars[i];
        } else {
            long l = (long)Math.pow(1024, i - 1D);
            if (round < 0) {
                round = 0;
            }
            if (round > 2) {
                round = 2;
            }
            int t = (int)Math.pow(10, round);
            long s = getSize(file) / l - size * 1024;
            s = (s / t);
            return size + "." + s + chars[i];
        }
    }

    public static void dump(File file, String[] strings) throws IOException {
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        if (!file.isFile()) {
            throw new FileNotFoundException();
        }
        if (strings == null) {
            throw new NoStringException();
        }
        try (OutputStream outputStream = new FileOutputStream(file)) {
            for (int i = 0; i < strings.length; i++) {
                if (i != 0) {
                    outputStream.write(new byte[]{'\n'});
                }
                outputStream.write(strings[i].getBytes());
                outputStream.flush();
            }
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * This Methods dumps a String to a file by calling 'dump(File file, byte[] bytes)'-
     *
     * @param file
     * @param s
     * @throws IOException
     */
    public static void dump(File file, String s) throws IOException {
        dump(file, s.getBytes());
    }

    /**
     * This Methods dumps a byte array to a file with OutputStream
     *
     * @param file
     * @param bytes
     * @throws IOException if File was not found or error occurred while dumping the bytes
     * @throws NoStringException if the byte array is null.
     */
    public static void dump(File file, byte[] bytes) throws IOException {
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        if (!file.isFile()) {
            throw new FileNotFoundException();
        }
        if (bytes == null) {
            throw new NoStringException();
        }
        try (OutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(bytes);
            outputStream.flush();
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * This method is a abstraction of the ResourceManager and is recommended when you only want to read 1 file without management or caching it.
     *
     * @param file
     * @return
     */
    public static byte[] fileContentFromResourceAsBytes(String file) {
        try {
            return FileUtils.class.getResourceAsStream("/" + file).readAllBytes();
        } catch (IOException e) {
            return new byte[0];
        }
    }

    /**
     * This method is a abstraction of the ResourceManager and is recommended when you only want to read 1 file without management or caching it.
     *
     * @param file
     * @return
     */
    public static String[] fileContentFromResourceAsString(String file) {
        try {
            byte[] bytes = FileUtils.class.getResourceAsStream("/" + file).readAllBytes();
            List<String> strings = new ArrayList<>();
            StringBuilder st = new StringBuilder();
            for (byte b : bytes) {
                if ((char)b == '\n') {
                    strings.add(st.toString());
                    st = new StringBuilder();
                } else {
                    st.append((char)b);
                }
            }
            if (st.length() > 0) {
                strings.add(st.toString());
            }
            return strings.toArray(new String[0]);
        } catch (IOException e) {
            return new String[0];
        }
    }

}