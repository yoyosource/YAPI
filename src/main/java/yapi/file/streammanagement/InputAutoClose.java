// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.file.streammanagement;

import yapi.file.management.FileCloseUtils;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;

public abstract class InputAutoClose extends Input {

    private boolean isClosed = false;

    {
        FileCloseUtils.addShutdownClose(this);
    }

    public InputAutoClose(String name) throws FileNotFoundException {
        super(name);
    }

    public InputAutoClose(File file) throws FileNotFoundException {
        super(file);
    }

    public InputAutoClose(FileDescriptor fdObj) {
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