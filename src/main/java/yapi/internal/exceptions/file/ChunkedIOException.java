// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.internal.exceptions.file;

import java.io.IOException;

public class ChunkedIOException extends IOException {

    public ChunkedIOException() {
    }

    public ChunkedIOException(String message) {
        super(message);
    }

    public ChunkedIOException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChunkedIOException(Throwable cause) {
        super(cause);
    }

}