// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.file.streams.base64;

import java.io.*;
import java.nio.channels.FileChannel;

public class Base64FileInputStream extends FileInputStream {

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
        return super.read();
    }

    @Override
    public int read(byte[] b) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long skip(long n) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int available() throws IOException {
        return super.available();
    }

    @Override
    public void close() throws IOException {
        super.close();
    }

    @Override
    public FileChannel getChannel() {
        return super.getChannel();
    }


    @Override
    public byte[] readAllBytes() throws IOException {
        return super.readAllBytes();
    }

    @Override
    public byte[] readNBytes(int len) throws IOException {
        return super.readNBytes(len);
    }

    @Override
    public int readNBytes(byte[] b, int off, int len) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void skipNBytes(long n) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public synchronized void mark(int readlimit) {
        throw new UnsupportedOperationException();
    }

    @Override
    public synchronized void reset() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean markSupported() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long transferTo(OutputStream out) throws IOException {
        return super.transferTo(out);
    }

}