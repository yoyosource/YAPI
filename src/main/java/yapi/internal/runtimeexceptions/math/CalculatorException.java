// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.internal.runtimeexceptions.math;

import yapi.internal.runtimeexceptions.MathException;

public class CalculatorException extends MathException {

    public CalculatorException() {
        super();
    }

    public CalculatorException(String message) {
        super(message);
    }

    public CalculatorException(String message, Throwable cause) {
        super(message, cause);
    }

    public CalculatorException(Throwable cause) {
        super(cause);
    }

    protected CalculatorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}