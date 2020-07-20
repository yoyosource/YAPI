// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.encryption.encryption;

import yapi.internal.exceptions.CipherException;

public final class YAPICipherCrypt {

    private YAPICipher yapiCipher;

    YAPICipherCrypt(YAPICipher yapiCipher) {
        this.yapiCipher = yapiCipher;
    }

    public byte[] crypt(byte[] key, byte[] bytes) throws CipherException {
        return yapiCipher.crypt(key, bytes);
    }

}