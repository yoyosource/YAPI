// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.file.streams.hex;

import yapi.math.base.BaseConversion;
import yapi.string.StringFormatting;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class HexFileInputStream extends FileInputStream {

    public HexFileInputStream(String name) throws FileNotFoundException {
        super(name);
    }

    public HexFileInputStream(File file) throws FileNotFoundException {
        super(file);
    }

    public HexFileInputStream(FileDescriptor fdObj) {
        super(fdObj);
    }

    @Override
    public int read() throws IOException {
        StringBuilder st = new StringBuilder();
        while (true) {
            byte b = (byte)super.read();
            if (b == ' ' || available() == 0) {
                break;
            }
            if ((b >= '0' && b <= '9') || (b >= 'A' && b <= 'F')) {
                st.append((char)b);
            } else {
                throw new IOException();
            }
        }
        return Integer.parseInt(st.toString(), 16);
    }

    @Override
    public int read(byte[] b) throws IOException {
        for (int i = 0; i < b.length; i++) {
            b[i] = (byte)read();
        }
        return super.read(b);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return super.read(b, off, len);
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
        return readNBytes(available());
    }

    @Override
    public byte[] readNBytes(int len) throws IOException {
        List<Byte> bufs = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            if (available() == 0) {
                break;
            }
            bufs.add((byte)read());
        }
        byte[] bytes = new byte[bufs.size()];
        for (int i = 0; i < bufs.size(); i++) {
            bytes[i] = bufs.get(i);
        }
        return bytes;
    }

    @Override
    public int readNBytes(byte[] b, int off, int len) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void skipNBytes(long n) throws IOException {
        throw new UnsupportedOperationException();
    }

}