// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.encryption.encryption;

import yapi.internal.exceptions.CipherException;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.NoSuchAlgorithmException;

public final class YAPICipher {

    public static final String ENCRYPTION = "encryption";
    public static final String DECRYPTION = "decryption";

    public static final String ENCRYPTION_PARALLEL = "encryption_parallel";
    public static final String DECRYPTION_PARALLEL = "decryption_parallel";

    public static final String DERIVATION = "derivation";

    public static YAPICipher getInstance(String mode) throws NoSuchAlgorithmException {
        if (mode.equals(ENCRYPTION)) {
            return new YAPICipher(new YAPICipherModeEncryption());
        }
        if (mode.equals(DECRYPTION)) {
            return new YAPICipher(new YAPICipherModeDecryption());
        }

        if (mode.equals(ENCRYPTION_PARALLEL)) {
            return new YAPICipher(new YAPICipherModeEncryptionParallel());
        }
        if (mode.equals(DECRYPTION_PARALLEL)) {
            return new YAPICipher(new YAPICipherModeDecryptionParallel());
        }

        if (mode.equalsIgnoreCase(DERIVATION)) {
            return new YAPICipher(new YAPICipherModeDerivation());
        }
        throw new NoSuchAlgorithmException();
    }

    private YAPICipherImpl yapiCipher;
    private boolean used = false;

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
        if (used) {
            throw new IllegalStateException();
        }
        used = true;
        try {
            return yapiCipher.crypt(key, bytes);
        } catch (CipherException e) {
            used = false;
            throw e;
        }
    }

    /**
     * Implementations that use this method:
     * - encryption_parallel
     * - decryption_parallel
     *
     * @param key
     * @param source
     * @param destination
     * @param threads
     * @throws CipherException
     */
    public void cryptParallel(byte[] key, FileInputStream source, FileOutputStream destination, int threads) throws CipherException {
        if (used) {
            throw new IllegalStateException();
        }
        used = true;
        try {
            yapiCipher.cryptParallel(key, source, destination, threads);
        } catch (CipherException e) {
            used = false;
            throw e;
        }
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
        if (used) {
            throw new IllegalStateException();
        }
        used = true;
        try {
            return yapiCipher.derive(salt, key, size);
        } catch (CipherException e) {
            used = false;
            throw e;
        }
    }

    public byte[] execute(YAPICipherDescription description) throws CipherException {
        switch (description.type) {
            case CRYPT:
                return crypt(description.key, description.bytes);
            case CRYPT_PARALLEL:
                cryptParallel(description.key, description.source, description.destination, description.threads);
                return new byte[0];
            case DERIVE:
                return derive(description.salt, description.key, description.size);
            default:
                return new byte[0];
        }
    }

}