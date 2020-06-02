// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.internal.runtimeexceptions;

public class YAPIRuntimeException extends RuntimeException {

    public YAPIRuntimeException() {
        super();
    }

    public YAPIRuntimeException(String message) {
        super(message);
    }

    public YAPIRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public YAPIRuntimeException(Throwable cause) {
        super(cause);
    }

    protected YAPIRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}