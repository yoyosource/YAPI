// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.file.streammanagement;

import yapi.file.management.FileCloseUtils;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;

public abstract class OutputAutoClose extends Output {

    private boolean isClosed = false;

    {
        FileCloseUtils.addShutdownClose(this);
    }

    public OutputAutoClose(String name) throws FileNotFoundException {
        super(name);
    }

    public OutputAutoClose(String name, boolean append) throws FileNotFoundException {
        super(name, append);
    }

    public OutputAutoClose(File file) throws FileNotFoundException {
        super(file);
    }

    public OutputAutoClose(File file, boolean append) throws FileNotFoundException {
        super(file, append);
    }

    public OutputAutoClose(FileDescriptor fdObj) {
        super(fdObj);
    }

    @Override
    public final void close() throws IOException {
        if (isClosed) {
            return;
        }
        isClosed = true;

        closeStream();
        super.close();
    }

    public abstract void closeStream() throws IOException;

}