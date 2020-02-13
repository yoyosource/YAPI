// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.exceptions.math;

import yapi.exceptions.MathException;

public class SetException extends MathException {

    public SetException() {
        super();
    }

    public SetException(String message) {
        super(message);
    }

    public SetException(String message, Throwable cause) {
        super(message, cause);
    }

    public SetException(Throwable cause) {
        super(cause);
    }

    protected SetException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}