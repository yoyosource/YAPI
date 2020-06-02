// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.file.streams.base.base64;

import yapi.file.streammanagement.Input;
import yapi.math.base.BaseConversion;

import java.io.*;

public class Base64FileInputStream extends Input {

    private StringBuilder st = new StringBuilder();

    public Base64FileInputStream(String name) throws FileNotFoundException {
        super(name);
    }

    public Base64FileInputStream(File file) throws FileNotFoundException {
        super(file);
    }

    public Base64FileInputStream(FileDescriptor fdObj) {
        super(fdObj);
    }

    @Override
    public int read() throws IOException {
        while (st.length() < 8) {
            int b = super.read();
            if (b == -1) {
                close();
            } else if ((char)b == '=') {
                st.append("00");
            } else {
                st.append(Base64Utils.getString((char) b));
            }
        }
        String bt = st.substring(0, 8);
        st.delete(0, 8);

        return BaseConversion.fromBase2toInt(bt);
    }

    @Override
    public void close() throws IOException {
        super.close();
    }

}