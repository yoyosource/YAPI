// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.file.zip;

import yapi.internal.annotations.yapi.WorkInProgress;
import yapi.internal.annotations.yapi.WorkInProgressType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.ZipInputStream;

@WorkInProgress(context = WorkInProgressType.ALPHA)
public class ZIPInflater {

    private File file;
    private ZipInputStream inputStream;

    public ZIPInflater(File f) throws IOException {
        if (!f.exists()) {
            throw new FileNotFoundException();
        }
        if (!f.isFile()) {
            throw new IOException("File needed");
        }

        this.file = f;
        this.inputStream = new ZipInputStream(new FileInputStream(f));
    }

    public synchronized void unzip(File destination) throws IOException {
        if (!destination.exists()) {
            destination.mkdirs();
        }
        if (!destination.isDirectory()) {
            throw new IOException("Directory needed");
        }
    }

}