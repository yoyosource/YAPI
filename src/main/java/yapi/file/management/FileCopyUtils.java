// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.file.management;

import yapi.file.FileUtils;
import yapi.internal.annotations.yapi.WorkInProgress;
import yapi.internal.annotations.yapi.WorkInProgressType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileCopyUtils {

    public static boolean copyFile(File source, File destination) {
        if (source.getAbsolutePath().equals(destination.getAbsolutePath())) {
            return false;
        }

        if (!source.isFile() || !source.exists()) {
            return false;
        }
        if (!destination.exists()) {
            try {
                FileUtils.create(destination);
            } catch (IOException e) {
                return false;
            }
        }
        if (!destination.isFile()) {
            return false;
        }

        try (FileInputStream fileInputStream = new FileInputStream(source)) {
            fileInputStream.transferTo(new FileOutputStream(destination));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            destination.delete();
        }
        return false;
    }

    @WorkInProgress(context = WorkInProgressType.ALPHA)
    public static boolean copyDir(File source, File destination) {
        if (source.getAbsolutePath().equals(destination.getAbsolutePath())) {
            return false;
        }

        if (!source.isDirectory() || !source.exists()) {
            return false;
        }
        if (!destination.exists()) {
            destination.mkdirs();
        }
        if (!destination.isDirectory()) {
            return false;
        }
        return false;
    }

}