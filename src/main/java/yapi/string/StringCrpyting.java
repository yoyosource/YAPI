package yapi.string;

import yapi.encryption.EncryptionSymmetric;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StringCrpyting {

    public static final String MD5 = "MD5";
    public static final String SHA1 = "SHA-1";
    public static final String SHA256 = "SHA-256";
    public static final String SHA512 = "SHA-512";
    public static final String[] hashTypes = new String[]{MD5, SHA1, SHA256, SHA512};

    private StringCrpyting() {
        throw new IllegalStateException("Utility class");
    }

    public static String toHex(byte[] bytes) {
        return StringUtils.toHex(bytes);
    }

    public static String toHex(byte[] bytes, boolean spaces) {
        return StringUtils.toHex(bytes, spaces);
    }

    public static byte[] hash(String s) {
        return hash(s, SHA512);
    }

    public static byte[] hash(String s, String hashType) {
        boolean b = false;
        for (String t : hashTypes) {
            if (t.equals(hashType)) {
                b = true;
            }
        }
        if (!b) {
            hashType = SHA512;
        }
        try {
            MessageDigest digest = MessageDigest.getInstance(hashType);
            return digest.digest((s + "").getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            return new byte[0];
        }
    }

    public static byte[] encrypt(String s, String password) {
        String key = EncryptionSymmetric.createKey(password, 4);
        return EncryptionSymmetric.encrypt(s.getBytes(), key);
    }

    public static byte[] encrypt(String s, String userName, String password) {
        String key = EncryptionSymmetric.createKey(userName, password, 4);
        return EncryptionSymmetric.encrypt(s.getBytes(), key);
    }

    public static byte[] encrypt(String s, String password, int security) {
        String key = EncryptionSymmetric.createKey(password, security);
        return EncryptionSymmetric.encrypt(s.getBytes(), key);
    }

    public static byte[] encrypt(String s, String userName, String password, int security) {
        String key = EncryptionSymmetric.createKey(userName, password, security);
        return EncryptionSymmetric.encrypt(s.getBytes(), key);
    }

    public static String decrypt(byte[] bytes, String password) {
        String key = EncryptionSymmetric.createKey(password, 4);
        byte[] r = EncryptionSymmetric.decrypt(bytes, key);
        return StringUtils.toString(r);
    }

    public static String decrypt(byte[] bytes, String userName, String password) {
        String key = EncryptionSymmetric.createKey(userName, password, 4);
        byte[] r = EncryptionSymmetric.decrypt(bytes, key);
        return StringUtils.toString(r);
    }

    public static String decrypt(byte[] bytes, String password, int security) {
        String key = EncryptionSymmetric.createKey(password, security);
        byte[] r = EncryptionSymmetric.decrypt(bytes, key);
        return StringUtils.toString(r);
    }

    public static String decrypt(byte[] bytes, String userName, String password, int security) {
        String key = EncryptionSymmetric.createKey(userName, password, security);
        byte[] r = EncryptionSymmetric.decrypt(bytes, key);
        return StringUtils.toString(r);
    }

}
