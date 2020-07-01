// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.file.streams.base.morse;

import yapi.file.streammanagement.Input;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MorseFileInputStream extends Input {

    public MorseFileInputStream(String name) throws FileNotFoundException {
        super(name);
    }

    public MorseFileInputStream(File file) throws FileNotFoundException {
        super(file);
    }

    public MorseFileInputStream(FileDescriptor fdObj) {
        super(fdObj);
    }

    @Override
    public int read() throws IOException {
        StringBuilder st = new StringBuilder();
        int c;
        while ((c = super.read()) != ' ') {
            if (c == -1) {
                throw new IOException();
            }
            st.append(c);
        }
        if (st.length() == 0) {
            return ' ';
        }
        if (!MorseUtils.validMorse(st.toString())) {
            return '\u0000';
        }
        return MorseUtils.getChar(st.toString());
    }

    @Override
    public byte[] readNBytes(int len) throws IOException {
        List<Integer> bytes = new ArrayList<>();
        try {
            while (true) {
                bytes.add(read());
                if (bytes.size() >= len) {
                    break;
                }
            }
        } catch (IOException e) {

        }

        byte[] bts = new byte[bytes.size()];
        for (int i = 0; i < bytes.size(); i++) {
            bts[i] = (byte)(int)bytes.get(i);
        }
        return bts;
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
    public byte[] readAllBytes() throws IOException {
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
}