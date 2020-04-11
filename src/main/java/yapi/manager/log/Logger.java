// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.manager.log;

import java.util.UUID;

public class Logger {

    final String uuid = UUID.randomUUID().toString();
    String watchKey;

    public String getUuid() {
        return uuid;
    }

    public String getWatchKey() {
        return watchKey;
    }

    void setWatchKey(String watchKey) {
        this.watchKey = watchKey;
    }

    public boolean hasLog() {
        //return LogManager.hasNewLog(this);
        return false;
    }

    public String getLog() {
        if (!hasLog()) {
            return "";
        }
        //return LogManager.getLog(this);
        return "";
    }

}