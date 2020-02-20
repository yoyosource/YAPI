// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.internal.exceptions.math;

import yapi.internal.exceptions.MathException;

public class MathScriptException extends MathException {

    public MathScriptException() {
        super();
    }

    public MathScriptException(String message) {
        super(message);
    }

    public MathScriptException(String message, Throwable cause) {
        super(message, cause);
    }

    public MathScriptException(Throwable cause) {
        super(cause);
    }

    protected MathScriptException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}