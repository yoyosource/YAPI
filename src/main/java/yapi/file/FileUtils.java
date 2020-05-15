// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.file;

import yapi.internal.exceptions.string.NoStringException;
import yapi.math.NumberUtils;
import yapi.os.OSCheck;
import yapi.os.OSType;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
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
        while (size >= 1024) {
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
            if (l == 0) {
                return size + "." + "0".repeat(round) + chars[i];
            }
            long t = (long)Math.pow(10, round);
            long s = getSize(file) / l - size * 1024;
            s = (s / t);
            return size + "." + s + chars[i];
        }
    }

    public static String getSize(long size, boolean convert, boolean floating, int round) {
        if (!convert) {
            return size + "";
        }
        int i = 0;
        long oldSize = size;
        while (size >= 1024) {
            size /= 1024;
            i++;
        }
        char[] chars = new char[]{'b', 'K', 'M', 'G', 'T', 'P', 'Y'};
        if (!floating) {
            return size + "" + chars[i];
        } else {
            if (round < 0) {
                round = 0;
            }
            if (round > 2) {
                round = 2;
            }

            return NumberUtils.round(oldSize / Math.pow(1024, i), round) + "" + chars[i];
        }
    }

    public static void create(File file) throws IOException {
        if (file.exists()) {
            return;
        }
        String parent = file.getParent();
        new File(parent).mkdirs();
        file.createNewFile();
    }

    public static void dump(File file, String[] strings) throws IOException {
        dump(file, strings, false);
    }

    public static void dump(File file, String[] strings, boolean append) throws IOException {
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        if (!file.isFile()) {
            throw new FileNotFoundException();
        }
        if (strings == null) {
            throw new NoStringException();
        }
        try (OutputStream outputStream = new FileOutputStream(file, append)) {
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

    public static void dump(File file, String s, boolean append) throws IOException {
        dump(file, s.getBytes(), append);
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
        dump(file, bytes, false);
    }

    public static void dump(File file, byte[] bytes, boolean append) throws IOException {
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        if (!file.isFile()) {
            throw new FileNotFoundException();
        }
        if (bytes == null) {
            throw new NoStringException();
        }
        try (OutputStream outputStream = new FileOutputStream(file, append)) {
            outputStream.write(bytes);
            outputStream.flush();
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
     *
     * @param file
     * @return
     */
    public static InputStream fileFromResourceAsStream(String file) {
        return FileUtils.class.getResourceAsStream("/" + file);
    }

    /**
     * This method is a abstraction of the ResourceManager and is recommended when you only want to read 1 file without management or caching it.
     *
     * @param file
     * @return
     */
    public static byte[] fileContentFromResourceAsBytes(String file) {
        try {
            return fileFromResourceAsStream(file).readAllBytes();
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
            byte[] bytes = fileFromResourceAsStream(file).readAllBytes();
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

    public static void emptyFile(File file) {
        if (!file.isFile()) {
            return;
        }
        if (getSize(file) == 0) {
            return;
        }
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {

        }
    }

    public static String rwx(File file) {
        return rwx(file, false);
    }

    public static String rwx(File file, boolean extended) {
        StringBuilder st = new StringBuilder();
        if (file.isDirectory()) {
            st.append("d");
        } else if (Files.isSymbolicLink(Path.of(file.getPath()))) {
            st.append("l");
        } else {
            st.append("-");
        }
        if (file.canRead()) {
            st.append("r");
        } else {
            st.append("-");
        }
        if (file.canWrite()) {
            st.append("w");
        } else {
            st.append("-");
        }
        if (file.canExecute()) {
            st.append("x");
        } else {
            st.append("-");
        }
        if (!extended) {
            return st.toString();
        }
        if (file.isHidden()) {
            st.append("h");
        } else {
            st.append("-");
        }
        return st.toString();
    }

    public static long getDiskSpace() {
        return getMainMountPoint().getTotalSpace();
    }

    public static long getTotalSpace() {
        return getDiskSpace();
    }

    public static long getFreeSpace() {
        return getMainMountPoint().getFreeSpace();
    }

    public static long getUsableSpace() {
        return getMainMountPoint().getUsableSpace();
    }

    public static File getMainMountPoint() {
        if (OSCheck.getType() == OSType.WINDOWS) {
            return new File("C:");
        } else if (OSCheck.getType() == OSType.OTHER) {
            throw new NullPointerException();
        } else {
            return new File("/");
        }
    }

}