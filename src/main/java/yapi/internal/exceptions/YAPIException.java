// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.internal.exceptions;

public class YAPIException extends RuntimeException {

    public YAPIException() {
        super();
    }

    public YAPIException(String message) {
        super(message);
    }

    public YAPIException(String message, Throwable cause) {
        super(message, cause);
    }

    public YAPIException(Throwable cause) {
        super(cause);
    }

    protected YAPIException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}