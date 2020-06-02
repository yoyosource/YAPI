// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.file.streams.base.morse;

import yapi.file.streammanagement.Output;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MorseFileOutputStream extends Output {

    public MorseFileOutputStream(String name) throws FileNotFoundException {
        super(name);
    }

    public MorseFileOutputStream(String name, boolean append) throws FileNotFoundException {
        super(name, append);
    }

    public MorseFileOutputStream(File file) throws FileNotFoundException {
        super(file);
    }

    public MorseFileOutputStream(File file, boolean append) throws FileNotFoundException {
        super(file, append);
    }

    public MorseFileOutputStream(FileDescriptor fdObj) {
        super(fdObj);
    }

    @Override
    public void write(int b) throws IOException {
        if (!MorseUtils.validChar((char)b)) {
            return;
        }

        if (b == ' ') {
            super.write(' ');
            super.write(' ');
            return;
        }

        super.write(MorseUtils.getMorse((char)b).getBytes());
        super.write(' ');
    }

    @Override
    public void write(byte[] b) throws IOException {
        for (byte t : b) write(t);
    }
}