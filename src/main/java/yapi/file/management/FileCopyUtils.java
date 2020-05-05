// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.file.management;

import yapi.file.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileCopyUtils {

    public static void copyFile(File source, File destination) {
        if (source.getAbsolutePath().equals(destination.getAbsolutePath())) {
            return;
        }

        if (!source.isFile() || !source.exists()) {
            return;
        }
        if (!destination.exists()) {
            try {
                FileUtils.create(destination);
            } catch (IOException e) {
                return;
            }
        }
        if (!destination.isFile()) {
            return;
        }

        try (FileInputStream fileInputStream = new FileInputStream(source)) {
            fileInputStream.transferTo(new FileOutputStream(destination));
        } catch (IOException e) {
            e.printStackTrace();
            destination.delete();
        }
    }

    public static void copyDir(File source, File destination) {
        if (source.getAbsolutePath().equals(destination.getAbsolutePath())) {
            return;
        }

        if (!source.isDirectory() || !source.exists()) {
            return;
        }
        if (!destination.exists()) {
            destination.mkdirs();
        }
        if (!destination.isDirectory()) {
            return;
        }
    }

}