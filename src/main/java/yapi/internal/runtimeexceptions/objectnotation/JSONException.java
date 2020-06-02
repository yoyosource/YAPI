// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.internal.runtimeexceptions.objectnotation;

import yapi.internal.runtimeexceptions.ObjectNotationException;

public class JSONException extends ObjectNotationException {

    public JSONException() {
        super();
    }

    public JSONException(String message) {
        super(message);
    }

    public JSONException(String message, Throwable cause) {
        super(message, cause);
    }

    public JSONException(Throwable cause) {
        super(cause);
    }

    protected JSONException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}