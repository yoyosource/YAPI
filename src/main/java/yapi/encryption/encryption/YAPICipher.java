// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.encryption.encryption;

import yapi.internal.exceptions.CipherException;

import java.security.NoSuchAlgorithmException;

public class YAPICipher {

    public static final String ENCRYPTION = "encryption";
    public static final String DECRYPTION = "decryption";

    public static final String DERIVATION = "derivation";

    public static YAPICipher getInstance(String mode) throws NoSuchAlgorithmException {
        if (mode.equals(ENCRYPTION)) {
            return new YAPICipher(new YAPICipherModeEncryption());
        }
        if (mode.equals(DECRYPTION)) {
            return new YAPICipher(new YAPICipherModeDecryption());
        }

        if (mode.equalsIgnoreCase(DERIVATION)) {
            return new YAPICipher(new YAPICipherModeDerivation());
        }
        throw new NoSuchAlgorithmException();
    }

    private YAPICipherImpl yapiCipher;

    private YAPICipher(YAPICipherImpl yapiCipher) {
        this.yapiCipher = yapiCipher;
    }

    /**
     * Implementations that use this method:
     * - encryption
     * - decryption
     *
     * @param key
     * @param bytes
     * @return
     * @throws CipherException
     */
    public byte[] crypt(byte[] key, byte[] bytes) throws CipherException {
        return yapiCipher.crypt(key, bytes);
    }

    /**
     * Implementations that use this method:
     * - derivation
     *
     * @param salt
     * @param key
     * @param size
     * @return
     * @throws CipherException
     */
    public byte[] derive(byte[] salt, byte[] key, int size) throws CipherException {
        return yapiCipher.derive(salt, key, size);
    }

}