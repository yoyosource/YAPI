// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.manager.worker;

public class Task {

    private long taskID = 0;

    final void setTaskID(long id) {
        taskID = id;
    }

    /**
     * @since Version 1.1
     *
     * @return
     */
    public final long getTaskID() {
        return taskID;
    }

    /**
     * @since Version 1.1
     *
     * @return
     */
    public final long getID() {
        return getTaskID();
    }

    /**
     * @since Version 1.1
     */
    public void run() {
        // Should be overwritten by implementation
    }

}