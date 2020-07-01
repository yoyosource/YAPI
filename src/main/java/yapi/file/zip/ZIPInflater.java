// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.file.zip;

import yapi.internal.annotations.yapi.WorkInProgress;
import yapi.internal.annotations.yapi.WorkInProgressType;

import java.io.*;
import java.util.zip.ZipEntry;
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
    }

    public synchronized void unzip(File destination) throws IOException {
        this.inputStream = new ZipInputStream(new FileInputStream(file));
        if (!destination.exists()) {
            destination.mkdirs();
        }
        if (!destination.isDirectory()) {
            throw new IOException("Directory needed");
        }

        byte[] buffer = new byte[1024];
        ZipEntry entry = null;
        do {
            entry = inputStream.getNextEntry();
            if (entry == null) {
                continue;
            }

            String fileName = entry.getName();
            File f = new File(destination + "/" + fileName);

            if (entry.isDirectory()) {
                f.mkdirs();
            } else {
                FileOutputStream fileOutputStream = new FileOutputStream(f);
                int len;
                while ((len = inputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, len);
                }
                fileOutputStream.close();
                inputStream.closeEntry();
            }
        } while (entry != null);
    }

}