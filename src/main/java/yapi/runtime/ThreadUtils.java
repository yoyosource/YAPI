// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.runtime;

public class ThreadUtils {

    private ThreadUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static final ThreadGroup yapiGroup = new ThreadGroup("YAPI");

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void sleep(long millis, int nanos) {
        try {
            Thread.sleep(millis, nanos);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void sleep(double d) {
        sleep((long)d, (int)((d - (long)d) * 1000000));
    }

    public static void exactSleep(long millis) {
        long time = System.currentTimeMillis();
        while (System.currentTimeMillis() - time < millis) {
            sleep(millis / 100);
        }
    }

}