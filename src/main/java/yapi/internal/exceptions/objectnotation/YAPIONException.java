// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.internal.exceptions.objectnotation;

import yapi.internal.exceptions.ObjectNotationException;

public class YAPIONException extends ObjectNotationException {

    public YAPIONException() {
        super();
    }

    public YAPIONException(String message) {
        super(message);
    }

    public YAPIONException(String message, Throwable cause) {
        super(message, cause);
    }

    public YAPIONException(Throwable cause) {
        super(cause);
    }

    protected YAPIONException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}