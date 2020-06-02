// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.file.streams.base.hex;

import yapi.string.StringFormatting;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.Objects;

public class HexFileOutputStream extends FileOutputStream {

    public HexFileOutputStream(String name) throws FileNotFoundException {
        super(name);
    }

    public HexFileOutputStream(String name, boolean append) throws FileNotFoundException {
        super(name, append);
    }

    public HexFileOutputStream(File file) throws FileNotFoundException {
        super(file);
    }

    public HexFileOutputStream(File file, boolean append) throws FileNotFoundException {
        super(file, append);
    }

    public HexFileOutputStream(FileDescriptor fdObj) {
        super(fdObj);
    }

    @Override
    public void write(int b) throws IOException {
        super.write(StringFormatting.toHex(b).getBytes());
        super.write(' ');
    }

    @Override
    public void write(byte[] b) throws IOException {
        super.write(StringFormatting.toHex(b, true).getBytes());
        super.write(' ');
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        Objects.checkFromIndexSize(off, len, b.length);
        for (int i = 0 ; i < len ; i++) {
            write(b[off + i]);
        }
    }

    @Override
    public void close() throws IOException {
        super.close();
    }

    @Override
    public FileChannel getChannel() {
        return super.getChannel();
    }

}