// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.string;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.zip.CRC32;

public class StringCrypting {

    private StringCrypting() {
        throw new IllegalStateException("Utility class");
    }

    public static byte[] hash(String s) {
        return hash(s, HashType.SHA512);
    }

    public static String hash(String s, boolean spaces) {
        return StringFormatting.toHex(hash(s), spaces);
    }

    public static byte[] hash(String s, HashType hashType) {
        try {
            MessageDigest digest = MessageDigest.getInstance(hashType.getType());
            return digest.digest((s).getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            return new byte[0];
        }
    }

    public static byte[] hash(byte[] bytes, HashType hashType) {
        try {
            MessageDigest digest = MessageDigest.getInstance(hashType.getType());
            return digest.digest(bytes);
        } catch (NoSuchAlgorithmException e) {
            return new byte[0];
        }
    }

    public static String hash(String s, HashType hashType, boolean spaces) {
        return StringFormatting.toHex(hash(s, hashType), spaces);
    }

    public static String encodeBase64(String s) {
        return new String(Base64.getEncoder().encode(s.getBytes()), StandardCharsets.UTF_8);
    }

    public static String encodeBase64(byte... s) {
        return new String(Base64.getEncoder().encode(s), StandardCharsets.UTF_8);
    }

    public static String decodeBase64(String base64) {
        return new String(Base64.getDecoder().decode(base64.getBytes()), StandardCharsets.UTF_8);
    }

    public static String decodeBase64(byte... base64) {
        return new String(Base64.getDecoder().decode(base64), StandardCharsets.UTF_8);
    }

    public static String checksum(String s) {
        CRC32 crc32 = new CRC32();
        for (char c : s.toCharArray()) {
            crc32.update((byte)(c >> 8));
            crc32.update((byte)((c << 8) >> 8));
        }
        return StringFormatting.toHex(crc32.getValue());
    }

    public static String checksum(byte... bytes) {
        CRC32 crc32 = new CRC32();
        for (byte b : bytes) {
            crc32.update(b);
        }
        return StringFormatting.toHex(crc32.getValue());
    }

}