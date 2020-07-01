// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.file.streammanagement;

import java.io.*;
import java.nio.channels.FileChannel;

public class Input extends FileInputStream {

    public Input(String name) throws FileNotFoundException {
        super(name);
    }

    public Input(File file) throws FileNotFoundException {
        super(file);
    }

    public Input(FileDescriptor fdObj) {
        super(fdObj);
    }

    @Override
    public int read() throws IOException {
        return super.read();
    }

    @Override
    public int read(byte[] b) throws IOException {
        return super.read(b);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return super.read(b, off, len);
    }

    @Override
    public long skip(long n) throws IOException {
        return super.skip(n);
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
        return super.readNBytes(b, off, len);
    }

    @Override
    public void skipNBytes(long n) throws IOException {
        super.skipNBytes(n);
    }

    @Override
    public synchronized void mark(int readlimit) {
        super.mark(readlimit);
    }

    @Override
    public synchronized void reset() throws IOException {
        super.reset();
    }

    @Override
    public boolean markSupported() {
        return super.markSupported();
    }

    @Override
    public long transferTo(OutputStream out) throws IOException {
        return super.transferTo(out);
    }

}