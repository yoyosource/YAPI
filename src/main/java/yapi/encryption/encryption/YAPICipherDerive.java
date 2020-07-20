// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.encryption.encryption;

import yapi.internal.exceptions.CipherException;

public final class YAPICipherDerive {

    private YAPICipher yapiCipher;

    YAPICipherDerive(YAPICipher yapiCipher) {
        this.yapiCipher = yapiCipher;
    }

    public byte[] derive(byte[] salt, byte[] key, int size) throws CipherException {
        return yapiCipher.derive(salt, key, size);
    }

}