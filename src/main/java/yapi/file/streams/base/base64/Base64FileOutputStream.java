// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.file.streams.base.base64;

import yapi.file.management.FileCloseUtils;
import yapi.file.streammanagement.OutputAutoClose;
import yapi.math.base.BaseConversion;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class Base64FileOutputStream extends OutputAutoClose {

    private boolean isClosed = false;

    {
        FileCloseUtils.addShutdownClose(this);
    }

    private StringBuilder st = new StringBuilder();
    private int i = 0;

    public Base64FileOutputStream(String name) throws FileNotFoundException {
        super(name);
    }

    public Base64FileOutputStream(String name, boolean append) throws FileNotFoundException {
        super(name, append);
    }

    public Base64FileOutputStream(File file) throws FileNotFoundException {
        super(file);
    }

    public Base64FileOutputStream(File file, boolean append) throws FileNotFoundException {
        super(file, append);
    }

    public Base64FileOutputStream(FileDescriptor fdObj) {
        super(fdObj);
    }

    @Override
    public void write(int b) throws IOException {
        String base2 = BaseConversion.toBase2(b);
        st.append("0".repeat(8 - base2.length())).append(base2);
        writeInternal();
    }

    private void writeInternal() throws IOException {
        while (st.length() >= 6) {
            String s = st.substring(0, 6);
            st.delete(0, 6);

            super.write(Base64Utils.getChar(s));
            if (i == 75) {
                super.write('\n');
            }
            i = (i + 1) % 76;
        }
    }

    @Override
    public void write(byte[] b) throws IOException {
        for (byte d : b) {
            write(d);
        }
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        for (int i = 0; i < len; i++) {
            write(b[i + off]);
        }
    }

    @Override
    public void closeStream() throws IOException {
        if (st.length() > 0) {
            int i = 6 - st.length();
            st.append("0".repeat(i));
            writeInternal();
            while (i > 0) {
                super.write('=');
                i -= 2;
            }
        }
    }

    @Override
    public FileChannel getChannel() {
        return super.getChannel();
    }

}