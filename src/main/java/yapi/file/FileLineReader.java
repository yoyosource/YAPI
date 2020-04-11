// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.file;

import java.io.IOException;
import java.io.InputStream;

public class FileLineReader {

    private InputStream inputStream;

    public FileLineReader(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String readLine() {
        StringBuilder st = new StringBuilder();
        while (true) {
            if (!hasNext()) {
                break;
            }
            char c = readChar();
            if (c == '\n') {
                break;
            }
            st.append(c);
        }
        return st.toString();
    }

    public Character readChar() {
        try {
            return (char) inputStream.read();
        } catch (IOException e) {
            return '\u0000';
        }
    }

    public boolean hasNext() {
        try {
            return inputStream.available() > 0;
        } catch (IOException e) {
            return false;
        }
    }

}