// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.exceptions;

public class StringException extends YAPIException {

    public StringException() {
        super();
    }

    public StringException(String message) {
        super(message);
    }

    public StringException(String message, Throwable cause) {
        super(message, cause);
    }

    public StringException(Throwable cause) {
        super(cause);
    }

    protected StringException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}