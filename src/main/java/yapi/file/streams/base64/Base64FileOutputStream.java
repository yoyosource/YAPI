// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.file.streams.base64;

import yapi.file.streams.FileCloseUtils;

import java.io.*;
import java.nio.channels.FileChannel;

public class Base64FileOutputStream extends FileOutputStream {

    private boolean isClosed = false;

    {
        FileCloseUtils.addShutdownClose(this);
    }

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
        super.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
        super.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        super.write(b, off, len);
    }

    @Override
    public void close() throws IOException {
        if (isClosed) {
            return;
        }
        isClosed = true;
        super.close();
    }

    @Override
    public FileChannel getChannel() {
        return super.getChannel();
    }

}