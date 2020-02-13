// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.exceptions;

public class ObjectNotationException extends YAPIException {

    public ObjectNotationException() {
        super();
    }

    public ObjectNotationException(String message) {
        super(message);
    }

    public ObjectNotationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ObjectNotationException(Throwable cause) {
        super(cause);
    }

    protected ObjectNotationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}