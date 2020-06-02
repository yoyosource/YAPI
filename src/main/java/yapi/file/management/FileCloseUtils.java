// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.file.management;

import yapi.runtime.Hook;
import yapi.runtime.RuntimeUtils;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileCloseUtils {

    private FileCloseUtils() {
        throw new IllegalStateException("Utility class");
    }

    private static boolean implementsCloseable(Object object) {
        return object instanceof Closeable;
    }

    public static void addShutdownClose(InputStream inputStream) {
        addShutdownHook(inputStream);
    }

    public static void addShutdownClose(OutputStream outputStream) {
        addShutdownHook(outputStream);
    }

    public static void addShutdownClose(Object object) {
        if (!implementsCloseable(object)) {
            throw new IllegalArgumentException("Object needs to implement Closeable");
        }
        addShutdownHook((Closeable) object);
    }

    private static void addShutdownHook(Closeable closeable) {
        RuntimeUtils.addSutdownHook(new Hook() {
            @Override
            public void run() {
                try {
                    closeable.close();
                } catch (IOException e) {

                }
            }
        });
    }

}