// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.file.streams.base.binary;

import yapi.file.streammanagement.Input;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BinaryFileInputStream extends Input {

    public BinaryFileInputStream(String name) throws FileNotFoundException {
        super(name);
    }

    public BinaryFileInputStream(File file) throws FileNotFoundException {
        super(file);
    }

    public BinaryFileInputStream(FileDescriptor fdObj) {
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
        return BinaryUtils.fromBinary(st.toString());
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
}