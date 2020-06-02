// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.internal.runtimeexceptions.string;

import yapi.internal.runtimeexceptions.StringException;

public class NoStringException extends StringException {

    public NoStringException() {
        super();
    }

    public NoStringException(String message) {
        super(message);
    }

    public NoStringException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoStringException(Throwable cause) {
        super(cause);
    }

    protected NoStringException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}