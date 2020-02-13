// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.exceptions.math;

import yapi.exceptions.MathException;

public class RangeException extends MathException {

    public RangeException() {
        super();
    }

    public RangeException(String message) {
        super(message);
    }

    public RangeException(String message, Throwable cause) {
        super(message, cause);
    }

    public RangeException(Throwable cause) {
        super(cause);
    }

    protected RangeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}