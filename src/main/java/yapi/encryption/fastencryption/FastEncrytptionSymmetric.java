// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.encryption.fastencryption;

import yapi.internal.exceptions.EncryptionException;
import yapi.math.NumberRandom;
import yapi.string.HashType;
import yapi.string.StringCrypting;
import yapi.string.StringFormatting;
import yapi.string.UTF8String;

import java.io.ByteArrayInputStream;
import java.util.Arrays;

public class FastEncrytptionSymmetric {

    public static String createKey(String password) {
        long l = 0;
        for (int i = 0; i < password.length(); i++) {
            l += password.charAt(i) * (i + 1);
        }
        long l1 = l;
        byte[] bytes = StringCrypting.hash(password, HashType.SHA512);
        for (int i = 0; i < bytes.length; i++) {
            l += bytes[i] * (i + 1);
        }
        return new NumberRandom(l - l1).getString(128) + new NumberRandom(l).getString(256) + new NumberRandom(l1).getString(128);
    }

    static String deriveKey(String key) {
        byte[] bytes = StringCrypting.hash(key, HashType.SHA512);
        long l = 0;
        for (int i = 0; i < bytes.length; i++) {
            l += bytes[i] * (i + 1);
        }

        StringBuilder st = new StringBuilder();
        for (int i = 0; i < 256; i++) {
            st.append(key.charAt(new NumberRandom(l).getInt(key.length())));
        }
        return new NumberRandom(l).getString(256) + st.toString();
    }

    private static byte[] pad() {
        return new NumberRandom().getString(32).getBytes();
    }

    private static void validateKey(String key) {
        if (key.length() != 512) {
            throw new EncryptionException("Invalid key length. Excepted: 512, Found: " + key.length());
        }
    }

    private static String[] keys(String key) {
        StringBuilder st = new StringBuilder();
        String[] strings = new String[key.length() / 16];
        int i = 0;
        for (char c : key.toCharArray()) {
            st.append(c);
            if (st.length() == 16) {
                strings[i] = st.toString();
                st = new StringBuilder();
                i++;
            }
        }
        return strings;
    }

    private static String[] createMasterKeys(String[] keys) {
        StringBuilder[] stringBuilders = new StringBuilder[keys[0].length()];
        for (int i = 0; i < stringBuilders.length; i++) {
            stringBuilders[i] = new StringBuilder();
        }

        for (String s : keys) {
            long l = 0;
            for (int i = 0; i < s.length(); i++) {
                l += s.charAt(i) * (i + 1);
            }

            NumberRandom numberRandom = new NumberRandom(l);
            StringBuilder st = new StringBuilder().append(s);
            for (int i = 0; i < 100; i++) {
                int index = numberRandom.getInt(st.length());
                char c = st.charAt(index);
                st.deleteCharAt(index).append(c);
            }
            for (int i = 0; i < stringBuilders.length; i++) {
                stringBuilders[i].append(st.charAt(i));
            }
        }

        String[] strings = new String[stringBuilders.length];
        for (int i = 0; i < stringBuilders.length; i++) {
            strings[i] = stringBuilders[i].toString();
        }
        return strings;
    }

    public static byte[] encrypt(String key, UTF8String bytes) {
        return encrypt(key, bytes.getRaw());
    }

    public static byte[] encrypt(String key, byte... bytes) {
        validateKey(key);
        String[] keys = keys(key);
        String[] masterKeys = createMasterKeys(keys);

        byte[] hash = StringCrypting.hash(bytes, HashType.SHA256);

        byte[] pad = pad();
        byte[] bytesStream = Arrays.copyOf(pad, pad.length + hash.length + bytes.length);
        for (int i = 0; i < hash.length; i++) {
            bytesStream[i + pad.length] = hash[i];
        }
        for (int i = 0; i < bytes.length; i++) {
            bytesStream[i + pad.length + hash.length] = bytes[i];
        }

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytesStream);

        int index = 0;
        int i = 0;
        while (byteArrayInputStream.available() > 0) {
            byte current = (byte)byteArrayInputStream.read();
            int t = (index < 0 ? index + 255 : index) % keys.length;
            String currentKey = keys[t];
            index = current;

            current = xorPlus(current, currentKey);
            current = xorPlus(current, masterKeys[i % masterKeys.length]);
            bytesStream[i] = current;

            i++;
        }

        return bytesStream;
    }

    public static byte[] decrypt(String key, UTF8String bytes) {
        return encrypt(key, bytes.getRaw());
    }

    public static byte[] decrypt(String key, byte... bytes) {
        validateKey(key);
        String[] keys = keys(key);
        String[] masterKeys = createMasterKeys(keys);

        byte[] bytesStream = new byte[bytes.length];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

        int index = 0;
        int i = 0;
        while (byteArrayInputStream.available() > 0) {
            byte current = (byte)byteArrayInputStream.read();
            int t = (index < 0 ? index + 255 : index) % keys.length;
            String currentKey = keys[t];

            current = xorPlusInverse(current, masterKeys[i % masterKeys.length]);
            current = xorPlusInverse(current, currentKey);
            bytesStream[i] = current;

            index = bytesStream[i];
            i++;
        }

        bytes = new byte[bytesStream.length - 32];
        byte[] pad = pad();
        int length = pad.length;
        i = length;
        while (i < bytesStream.length) {
            bytes[i - length] = bytesStream[i];
            i++;
        }

        byte[] hash = Arrays.copyOf(bytes, length);
        bytesStream = new byte[bytes.length - length];
        i = length;
        while (i < bytes.length) {
            bytesStream[i - length] = bytes[i];
            i++;
        }

        System.out.println(StringFormatting.toHex(bytesStream));
        if (Arrays.equals(StringCrypting.hash(bytesStream, HashType.SHA256), hash)) {
            return bytesStream;
        } else {
            return new byte[0];
        }
    }

    private static byte xorPlus(byte b, String key) {
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            b ^= c;
            b += c;
        }
        return b;
    }

    private static byte xorPlusInverse(byte b, String key) {
        for (int i = key.length() - 1; i >= 0; i--) {
            char c = key.charAt(i);
            b -= c;
            b ^= c;
        }
        return b;
    }

}