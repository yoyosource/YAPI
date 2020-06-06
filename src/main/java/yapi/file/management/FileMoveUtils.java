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

public class FileMoveUtils {

    public static boolean moveFile(File source, File destination) {
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
            source.delete();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @WorkInProgress(context = WorkInProgressType.ALPHA)
    public static boolean moveDir(File source, File destination) {
        if (source.getAbsolutePath().equals(destination.getAbsolutePath())) {
            return false;
        }
        if (destination.getAbsolutePath().startsWith(source.getAbsolutePath())) {
            return false;
        }
        if (source.getAbsolutePath().startsWith(destination.getAbsolutePath())) {
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

        File[] files = source.listFiles();
        boolean b = true;
        for (File f : files) {
            if (f.isFile()) {
                b &= moveFile(f, new File(destination.getAbsolutePath() + "/" + f.getName()));
            } else {
                b &= moveDir(f, new File(destination.getAbsolutePath() + "/" + f.getName()));
            }
        }
        return b;
    }

}