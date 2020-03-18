// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.sorting.hook;

public class SortingHook<T> {

    public void hook(T... ts) {

    }

    private final void sleep(int sleepTime) {
        sleep((double)sleepTime);
    }

    private final void sleep(long sleepTime) {
        sleep((double)sleepTime);
    }

    private final void sleep(double d) {
        long millis = (long)d;
        int nanos = (int) ((d - millis) * 1000000);

        try {
            Thread.sleep(millis, nanos);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}