// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.internal.runtimeexceptions;

public class MathException extends YAPIRuntimeException {

    public MathException() {
        super();
    }

    public MathException(String message) {
        super(message);
    }

    public MathException(String message, Throwable cause) {
        super(message, cause);
    }

    public MathException(Throwable cause) {
        super(cause);
    }

    protected MathException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}