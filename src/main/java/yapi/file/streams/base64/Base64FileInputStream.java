// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.file.streams.base64;

import yapi.math.base.BaseConversion;

import java.io.*;
import java.nio.channels.FileChannel;

public class Base64FileInputStream extends FileInputStream {

    private StringBuilder st = new StringBuilder();
    private boolean isClosed = false;

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
                System.out.println("ENDE");
                close();
            } else if ((char)b == '=') {
                System.out.println("PADDING");
                st.append("00");
            } else {
                st.append(Base64Utils.getString((char) b));
            }
            System.out.println(st);
        }
        String bt = st.substring(0, 8);
        st.delete(0, 8);

        return BaseConversion.fromBase2toInt(bt);
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
        if (isClosed) {
            return;
        }
        isClosed = true;
        super.close();
    }

    public boolean isClosed() {
        return isClosed;
    }

    @Override
    public FileChannel getChannel() {
        return super.getChannel();
    }


    @Override
    public byte[] readAllBytes() throws IOException {
        // TODO: change this
        throw new UnsupportedOperationException();
    }

    @Override
    public byte[] readNBytes(int len) throws IOException {
        // TODO: change this
        throw new UnsupportedOperationException();
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