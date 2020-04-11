// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.manager.yapion.internal.parser;

import java.io.IOException;
import java.io.InputStream;

public class YAPIONReader {

    private InputStream inputStream;

    public YAPIONReader(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public Character readChar() throws IOException {
        return (char)inputStream.read();
    }

    public boolean hasNext() throws IOException {
        return inputStream.available() > 0;
    }

}