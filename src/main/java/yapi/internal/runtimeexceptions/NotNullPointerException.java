// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.internal.runtimeexceptions;

public class NotNullPointerException extends YAPIRuntimeException {

    public NotNullPointerException() {
        super();
    }

    public NotNullPointerException(String message) {
        super(message);
    }

    public NotNullPointerException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotNullPointerException(Throwable cause) {
        super(cause);
    }

    protected NotNullPointerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}