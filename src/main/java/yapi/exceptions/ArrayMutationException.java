// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.exceptions;

public class ArrayMutationException extends YAPIException {

    public ArrayMutationException() {
        super();
    }

    public ArrayMutationException(String message) {
        super(message);
    }

    public ArrayMutationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ArrayMutationException(Throwable cause) {
        super(cause);
    }

    protected ArrayMutationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}