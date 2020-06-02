// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.file.streams.base.binary;

import yapi.file.streammanagement.Output;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;

public class BinaryFileOutputStream extends Output {

    public BinaryFileOutputStream(String name) throws FileNotFoundException {
        super(name);
    }

    public BinaryFileOutputStream(String name, boolean append) throws FileNotFoundException {
        super(name, append);
    }

    public BinaryFileOutputStream(File file) throws FileNotFoundException {
        super(file);
    }

    public BinaryFileOutputStream(File file, boolean append) throws FileNotFoundException {
        super(file, append);
    }

    public BinaryFileOutputStream(FileDescriptor fdObj) {
        super(fdObj);
    }

    @Override
    public void write(int b) throws IOException {
        super.write(BinaryUtils.toBinary(b).getBytes());
        super.write(' ');
    }

    @Override
    public void write(byte[] b) throws IOException {
        for (byte t : b) write(t);
    }

}