// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.file.streammanagement;

import java.io.*;
import java.nio.channels.FileChannel;

public class Output extends FileOutputStream {

    public Output(String name) throws FileNotFoundException {
        super(name);
    }

    public Output(String name, boolean append) throws FileNotFoundException {
        super(name, append);
    }

    public Output(File file) throws FileNotFoundException {
        super(file);
    }

    public Output(File file, boolean append) throws FileNotFoundException {
        super(file, append);
    }

    public Output(FileDescriptor fdObj) {
        super(fdObj);
    }

    @Override
    public void write(int b) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void write(byte[] b) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void close() throws IOException {
        super.close();
    }

    @Override
    public FileChannel getChannel() {
        throw new UnsupportedOperationException();
    }


    @Override
    public void flush() throws IOException {
        super.flush();
    }
}