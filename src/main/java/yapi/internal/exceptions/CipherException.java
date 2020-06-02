// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.internal.exceptions;

public class CipherException extends YAPIException {
    public CipherException() {
        super();
    }

    public CipherException(String message) {
        super(message);
    }

    public CipherException(String message, Throwable cause) {
        super(message, cause);
    }

    public CipherException(Throwable cause) {
        super(cause);
    }

    protected CipherException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}