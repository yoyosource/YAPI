// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.file.zip;

import yapi.internal.annotations.yapi.WorkInProgress;
import yapi.internal.annotations.yapi.WorkInProgressType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.zip.ZipOutputStream;

@WorkInProgress(context = WorkInProgressType.ALPHA)
public class ZIPDeflater {

    private File output;
    private ZipOutputStream zipOutputStream;

    public ZIPDeflater(File output) throws IOException {
        this.output = output;
        if (!this.output.isFile()) {
            throw new FileNotFoundException();
        }
        if (!this.output.exists()) {
            this.output.createNewFile();
        }
        zipOutputStream = new ZipOutputStream(new PrintStream(this.output));
    }

    public void addFile(File f) {
        if (!f.exists()) return;
        if (!f.isFile()) return;

        //ZipEntry zipEntry = new ZipEntry()
    }

    public void close() {

    }

}