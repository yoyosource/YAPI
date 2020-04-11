// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.runtime;

public class Hook implements Runnable {

    private static long hookID = 0;

    private final long id = hookID++;

    public Hook() {

    }

    @Override
    public void run() {

    }

    public final long getId() {
        return id;
    }
}