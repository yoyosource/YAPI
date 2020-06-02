// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.internal.runtimeexceptions.string;

import yapi.internal.runtimeexceptions.StringException;

public class YAPIStringException extends StringException {

    public YAPIStringException() {
        super();
    }

    public YAPIStringException(String message) {
        super(message);
    }

    public YAPIStringException(String message, Throwable cause) {
        super(message, cause);
    }

    public YAPIStringException(Throwable cause) {
        super(cause);
    }

    protected YAPIStringException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}