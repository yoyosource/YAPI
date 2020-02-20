// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.string;

import yapi.encryption.EncryptionSymmetric;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.zip.CRC32;
import java.util.zip.CRC32C;

public class StringCrypting {

    private StringCrypting() {
        throw new IllegalStateException("Utility class");
    }

    public static String toHex(byte[] bytes) {
        return StringFormatting.toHex(bytes, true);
    }

    public static String toHex(byte[] bytes, boolean spaces) {
        return StringFormatting.toHex(bytes, spaces);
    }

    public static byte[] hash(String s) {
        return hash(s, HashType.SHA512);
    }

    public static String hash(String s, boolean spaces) {
        return toHex(hash(s), spaces);
    }

    public static byte[] hash(String s, HashType hashType) {
        try {
            MessageDigest digest = MessageDigest.getInstance(hashType.getType());
            return digest.digest((s + "").getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            return new byte[0];
        }
    }

    public static String hash(String s, HashType hashType, boolean spaces) {
        return toHex(hash(s, hashType), spaces);
    }

    public static byte[] encrypt(String s, String password) {
        String key = EncryptionSymmetric.createKey(password, 4, true);
        return EncryptionSymmetric.encrypt(s.getBytes(), key);
    }

    public static byte[] encrypt(String s, String userName, String password) {
        String key = EncryptionSymmetric.createKey(userName, password, 4, true);
        return EncryptionSymmetric.encrypt(s.getBytes(), key);
    }

    public static byte[] encrypt(String s, String password, int security) {
        String key = EncryptionSymmetric.createKey(password, security, true);
        return EncryptionSymmetric.encrypt(s.getBytes(), key);
    }

    public static byte[] encrypt(String s, String userName, String password, int security) {
        String key = EncryptionSymmetric.createKey(userName, password, security, true);
        return EncryptionSymmetric.encrypt(s.getBytes(), key);
    }

    public static String decrypt(byte[] bytes, String password) {
        String key = EncryptionSymmetric.createKey(password, 4, true);
        byte[] r = EncryptionSymmetric.decrypt(bytes, key);
        return StringFormatting.toString(r);
    }

    public static String decrypt(byte[] bytes, String userName, String password) {
        String key = EncryptionSymmetric.createKey(userName, password, 4, true);
        byte[] r = EncryptionSymmetric.decrypt(bytes, key);
        return StringFormatting.toString(r);
    }

    public static String decrypt(byte[] bytes, String password, int security) {
        String key = EncryptionSymmetric.createKey(password, security, true);
        byte[] r = EncryptionSymmetric.decrypt(bytes, key);
        return StringFormatting.toString(r);
    }

    public static String decrypt(byte[] bytes, String userName, String password, int security) {
        String key = EncryptionSymmetric.createKey(userName, password, security, true);
        byte[] r = EncryptionSymmetric.decrypt(bytes, key);
        return StringFormatting.toString(r);
    }

    public static String encodeBase64(String s) {
        return new String(Base64.getEncoder().encode(s.getBytes()), StandardCharsets.UTF_8);
    }

    public static String decodeBase64(String base64) {
        return new String(Base64.getDecoder().decode(base64), StandardCharsets.UTF_8);
    }

    public static String checksum(String s) {
        // CRC32
        // Todo: CRC32 implementation
        return "";
    }

}