// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.encryption;

import yapi.exceptions.EncryptionException;
import yapi.math.NumberRandom;
import yapi.string.StringCrpyting;
import yapi.string.StringFormatting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EncryptionSymmetric {

    private EncryptionSymmetric() {
        throw new IllegalStateException("Utility class");
    }

    public static final int XOR       = 0;
    public static final int NOT       = 1;
    public static final int INVERSE   = 1;

    public static final int PLUS      = 2;
    public static final int ADD       = 2;
    public static final int MINUS     = 3;
    public static final int SUBTRACT  = 3;

    public static final int XOR_PLUS  = 6;
    public static final int XOR_MINUS = 7;

    private static List<Integer> operations = new ArrayList<>();
    static {
        operations.add(XOR_PLUS);
    }

    public static void resetEncryption() {
        operations.clear();
        operations.add(XOR_PLUS);
    }

    public static void setEncryption() {
        resetEncryption();
    }

    public static void setEncryption(int... ints) {
        operations.clear();
        if (ints.length == 0) {
            operations.add(XOR_PLUS);
            return;
        }
        for (int i : ints) {
            if (i >= 0 && i <= 7) {
                operations.add(i);
            }
        }
    }

    public static void setEncryption(String operation) {
        operations.clear();
        if (operation.isBlank()) {
            operations.add(XOR_PLUS);
            return;
        }
        String[] strings = operation.split(" ");
        for (String s : strings) {
            if (s.equalsIgnoreCase("XOR") || s.equals(XOR + "")) {
                operations.add(XOR);
            } else if (s.equalsIgnoreCase("NOT") || s.equals(NOT + "")) {
                operations.add(NOT);
            } else if (s.equalsIgnoreCase("INVERSE")) {
                operations.add(INVERSE);
            } else if (s.equalsIgnoreCase("PLUS") || s.equals(PLUS + "")) {
                operations.add(PLUS);
            } else if (s.equalsIgnoreCase("ADD")) {
                operations.add(ADD);
            } else if (s.equalsIgnoreCase("MINUS") || s.equals(MINUS + "")) {
                operations.add(MINUS);
            } else if (s.equalsIgnoreCase("SUBTRACT")) {
                operations.add(SUBTRACT);
            } else if (s.equalsIgnoreCase("XOR_PLUS") || s.equals(XOR_PLUS + "")) {
                operations.add(XOR_PLUS);
            } else if (s.equalsIgnoreCase("XOR_MINUS") || s.equals(XOR_MINUS + "")) {
                operations.add(XOR_MINUS);
            }
        }
    }

    public static double checkPassword(String password) {
        return passwordStrength(password);
    }

    public static double passwordStrength(String password) {
        double strength = 0;
        strength += password.length() * 4;
        throw new IllegalStateException("Not done");
    }

    public static String createKey(String username, String password, int security, boolean newMode) {
        if (!newMode) {
            return createKey(username, password, security);
        }

        long checksum = 0;
        for (char c : username.toCharArray()) {
            checksum += c;
        }
        return rotate(createKey(password, security - 1, true) + createKey(username, security - 1, true), checksum);
    }

    @Deprecated(since = "Version 1.2, please use the new createKey method and convert old systems to new one. This feature will be removed in the Version 1.3.")
    public static String createKey(String username, String password, int security) {
        long checksum = 0;
        for (char c : username.toCharArray()) {
            checksum += c;
        }
        return rotate(createKey(password, security - 1) + createKey(username, security - 1), checksum);
    }

    public static String createKey(String password, int security, boolean newMode) {
        if (!newMode) {
            return createKey(password, security);
        }

        if (security < 0) {
            security = 0;
        }
        if (security > 16) {
            security = 16;
        }
        security = (int)Math.pow(2, security + 10.0);

        long checksum = 0;
        for (char c : password.toCharArray()) {
            checksum += c;
        }

        int hashsum = 0;
        byte[] hash = StringCrpyting.hash(password, "SHA-512");
        for (byte b : hash) {
            hashsum += b;
        }

        String key = toHex(hash).replace(" ", "") + new NumberRandom(checksum).getString(security / 2 - 128) + new NumberRandom(hashsum).getString(security / 2 - 128);
        key = toHex(hash).replace(" ", "") + mixUP(key, checksum + hashsum);
        return key;
    }

    @Deprecated(since = "Version 1.2, please use the new createKey method and convert old systems to new one. This feature will be removed in the Version 1.3.")
    public static String createKey(String password, int security) {
        if (security < 0) {
            security = 0;
        }
        if (security > 16) {
            security = 16;
        }
        security = (int)Math.pow(2, security + 8.0);

        long checksum = 0;
        for (char c : password.toCharArray()) {
            checksum += c;
        }

        int hashsum = 0;
        byte[] hash = StringCrpyting.hash(password, "SHA-256");
        for (byte b : hash) {
            hashsum += b;
        }

        String key = toHex(hash).replace(" ", "") + new NumberRandom(checksum).getString(security / 2 - 64) + new NumberRandom(hashsum).getString(security / 2 - 64);
        key = toHex(hash).replace(" ", "") + mixUP(key, checksum + hashsum);
        return key;
    }

    private static String mixUP(String key, long random) {
        NumberRandom numberRandom = new NumberRandom(random);
        char[] chars = key.toCharArray();
        for (int i = 0; i < 10000; i++) {
            int x1 = numberRandom.getInt(chars.length);
            int x2 = numberRandom.getInt(chars.length);
            char c = chars[x1];
            chars[x1] = chars[x2];
            chars[x2] = c;
        }
        StringBuilder st = new StringBuilder();
        for (char c : chars) {
            st.append(c);
        }
        return st.toString();
    }

    public static String createKey(int security) {
        if (security < 0) {
            security = 0;
        }
        if (security > 16) {
            security = 16;
        }
        security += 8;
        return new NumberRandom().getString((int)Math.pow(2, security));
    }

    public static String createKey() {
        return createKey(2);
    }

    private static String pad() {
        return new NumberRandom().getString(8);
    }

    private static String[] keys(String key) {
        StringBuilder st = new StringBuilder();
        String[] strings = new String[key.length() / 32];
        int i = 0;
        for (char c : key.toCharArray()) {
            st.append(c);
            if (st.length() == 32) {
                strings[i] = st.toString();
                st = new StringBuilder();
                i++;
            }
        }
        return strings;
    }

    private static boolean checkKey(int length) {
        if (length < 256) {
            return false;
        }
        int limit = 1;
        while (limit < length) {
            limit *= 2;
        }
        return length == limit;
    }

    public static String solve(String text, String password, int security) {
        if (text.matches("[A-F0-9]{2}( [A-F0-9]{2})*")) {
            return StringFormatting.formatText(toString(decrypt(toBytes(text), createKey(password, security))));
        }
        return toHex(encrypt(toBytes(StringFormatting.unformatText(text)), createKey(password, security)));
    }

    public static byte[] encrypt(byte[] text, String key) {
        if (!checkKey(key.length())) {
            throw new EncryptionException("Key needs to be at least 256 bytes long and a power of 2. Your key was " + key.length() + " bytes long.");
        }

        String[] keys = keys(key);
        byte[] bytes = new byte[text.length + pad().length() + 33];

        char[] pad = pad().toCharArray();
        byte[] checksum = StringCrpyting.hash(toString(text), "SHA-256");
        char[] chars = new char[bytes.length];
        for (int i = 0; i < pad.length; i++) {
            chars[i + 1] = pad[i];
        }
        for (int i = 0; i < checksum.length; i++) {
            chars[i + 1 + pad.length] = (char)checksum[i];
        }
        for (int i = 0; i < text.length; i++) {
            chars[i + 1 + pad.length + checksum.length] = (char)text[i];
        }

        for (int i = 1; i < bytes.length; i++) {

            int l = i - 1;
            int remove = (i < 8 ? i - 1 : 7);
            byte b = (byte)chars[i];

            for (int j = l; j >= l - remove; j--) {
                b = encrypt(b, rotate(keys[((int)chars[j] + 128) % keys.length], bytes[j] + (long)128));
            }
            for (int j = 0; j < keys.length; j++) {
                b = (encrypt(b, keys[j]));
            }

            bytes[i] = b;
        }

        byte b = 0;
        for (int i = 0; i < bytes.length; i++) {
            b += bytes[i] * i;
        }
        bytes[0] = b;

        NumberRandom numberRandom = new NumberRandom(b);
        for (int i = 0; i < 1000; i++) {
            int x1 = numberRandom.getInt(bytes.length - 1) + 1;
            int x2 = numberRandom.getInt(bytes.length - 1) + 1;
            b = bytes[x1];
            bytes[x1] = bytes[x2];
            bytes[x2] = b;
        }

        return bytes;
    }

    public static byte[] decrypt(byte[] bytes, String key) {
        if (!checkKey(key.length())) {
            throw new EncryptionException("Key needs to be at least 256 bytes long and a power of 2. Your key was " + key.length() + " bytes long.");
        }

        String[] keys = keys(key);
        byte[] intermediate = new byte[bytes.length];

        byte b = bytes[0];
        NumberRandom numberRandom = new NumberRandom(b);
        List<Integer> switches = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            switches.add(numberRandom.getInt(bytes.length - 1) + 1);
            switches.add(numberRandom.getInt(bytes.length - 1) + 1);
        }
        for (int i = switches.size() - 1; i >= 0; i -= 2) {
            int x1 = switches.get(i);
            int x2 = switches.get(i - 1);
            b = bytes[x1];
            bytes[x1] = bytes[x2];
            bytes[x2] = b;
        }

        bytes[0] = 0;

        for (int i = 1; i < bytes.length; i++) {

            int l = i - 1;
            int remove = (i < 8 ? i - 1 : 7);
            b = bytes[i];

            for (int j = keys.length - 1; j >= 0; j--) {
                b = (decrypt(b, keys[j]));
            }
            for (int j = l - remove; j <= l; j++) {
                b = decrypt(b, rotate(keys[((int)intermediate[j] + 128) % keys.length], bytes[j] + (long)128));
            }

            intermediate[i] = b;
        }

        int shift = pad().length() + 1;
        for (int i = shift; i < intermediate.length; i++) {
            intermediate[i - shift] = intermediate[i];
        }
        intermediate = Arrays.copyOf(intermediate, intermediate.length - shift);

        byte[] checksum1 = Arrays.copyOf(intermediate, 32);
        shift = 32;
        for (int i = shift; i < intermediate.length; i++) {
            intermediate[i - shift] = intermediate[i];
        }
        intermediate = Arrays.copyOf(intermediate, intermediate.length - shift);

        byte[] checksum2 = StringCrpyting.hash(toString(intermediate), "SHA-256");
        if (!Arrays.equals(checksum1, checksum2)) {
            return new byte[0];
        }

        return Arrays.copyOf(intermediate, intermediate.length);
    }

    private static byte encrypt(byte b, String key) {
        for (int i = 0; i < operations.size(); i++) {
            if (operations.get(i) == XOR) {
                b = xor(b, key);
            } else if (operations.get(i) == NOT) {
                b = not(b);
            } else if (operations.get(i) == PLUS) {
                b = plus(b, key);
            } else if (operations.get(i) == MINUS) {
                b = subtract(b, key);
            } else if (operations.get(i) == XOR_PLUS) {
                b = xorPlus(b, key);
            } else if (operations.get(i) == XOR_MINUS) {
                b = xorMinus(b, key);
            }
        }
        return b;
    }

    private static byte decrypt(byte b, String key) {
        for (int i = operations.size() - 1; i >= 0; i--) {
            if (operations.get(i) == XOR) {
                b = xor(b, key);
            } else if (operations.get(i) == NOT) {
                b = not(b);
            } else if (operations.get(i) == PLUS) {
                b = subtract(b, key);
            } else if (operations.get(i) == MINUS) {
                b = plus(b, key);
            } else if (operations.get(i) == XOR_PLUS) {
                b = xorPlusInverse(b, key);
            } else if (operations.get(i) == XOR_MINUS) {
                b = xorMinusInverse(b, key);
            }
        }
        return b;
    }

    private static byte xor(byte b, String key) {
        for (char c : key.toCharArray()) {
            b ^= c;
        }
        return b;
    }

    private static byte not(byte b) {
        b &= 0xFF;
        return b;
    }

    private static byte plus(byte b, String key) {
        for (char c : key.toCharArray()) {
            b += c;
        }
        return b;
    }

    private static byte subtract(byte b, String key) {
        for (char c : key.toCharArray()) {
            b -= c;
        }
        return b;
    }

    private static byte xorPlus(byte b, String key) {
        for (char c : key.toCharArray()) {
            b ^= c;
            b += c;
        }
        return b;
    }

    private static byte xorPlusInverse(byte b, String key) {
        char[] chars = key.toCharArray();
        for (int i = chars.length - 1; i >= 0; i--) {
            b -= chars[i];
            b ^= chars[i];
        }
        return b;
    }

    private static byte xorMinus(byte b, String key) {
        for (char c : key.toCharArray()) {
            b ^= c;
            b -= c;
        }
        return b;
    }

    private static byte xorMinusInverse(byte b, String key) {
        char[] chars = key.toCharArray();
        for (int i = chars.length - 1; i >= 0; i--) {
            b += chars[i];
            b ^= chars[i];
        }
        return b;
    }

    private static String rotate(String key, long rotation) {
        if (rotation % key.length() == 0) {
            return key;
        }

        rotation = rotation % key.length();
        char[] chars = key.toCharArray();
        char[] output = new char[key.length()];
        for (int i = 0; i < chars.length; i++) {
            output[(int)(i + rotation) % chars.length] = chars[i];
        }

        StringBuilder st = new StringBuilder();
        for (char c : output) {
            st.append(c);
        }
        return st.toString();
    }

    public static byte[] toBytes(String s) {
        if (s.matches("[A-F0-9]{2}( [A-F0-9]{2})*")) {
            String[] strings = s.split(" ");
            byte[] bytes = new byte[strings.length];
            for (int i = 0; i < strings.length; i++) {
                bytes[i] = (byte)(Integer.parseInt(strings[i], 16));
            }
            return bytes;
        } else {
            return s.getBytes();
        }
    }

    public static String toHex(char[] chars) {
        StringBuilder st = new StringBuilder();
        boolean t = false;
        for (char c : chars) {
            if (t) st.append(' ');
            st.append(String.format("%02X", (byte)c));
            t = true;
        }
        return st.toString();
    }

    public static String toHex(byte[] bytes) {
        StringBuilder st = new StringBuilder();
        boolean t = false;
        for (byte b : bytes) {
            if (t) st.append(' ');
            st.append(String.format("%02X", b));
            t = true;
        }
        return st.toString();
    }

    public static String toString(byte[] bytes) {
        StringBuilder st = new StringBuilder();
        for (byte b : bytes) {
            st.append((char)b);
        }
        return st.toString();
    }

}