// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.encryption.encryption;

import yapi.internal.exceptions.CipherException;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public final class YAPICipherCryptParallel {

    private YAPICipher yapiCipher;

    YAPICipherCryptParallel(YAPICipher yapiCipher) {
        this.yapiCipher = yapiCipher;
    }

    public void cryptParallel(byte[] key, FileInputStream source, FileOutputStream destination, int threads) throws CipherException {
        yapiCipher.cryptParallel(key, source, destination, threads);
    }

}