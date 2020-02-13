// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.manager.resource;

public class QueueEntry {

    private String name;
    private String path;
    private boolean async = false;

    /**
     *
     * @since Version 1.1
     *
     * @param name
     * @param path
     */
    public QueueEntry(String name, String path) {
        this.name = name;
        this.path = path;
    }

    /**
     *
     * @since Version 1.1
     *
     * @param name
     * @param path
     * @param async
     */
    public QueueEntry(String name, String path, boolean async) {
        this.name = name;
        this.path = path;
        this.async = async;
    }

    String getName() {
        return name;
    }

    String getPath() {
        return path;
    }

    boolean isAsync() {
        return async;
    }

}