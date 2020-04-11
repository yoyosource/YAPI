// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.manager.log;

import yapi.math.base.BaseConversion;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public class LogEntry {

    private LogUUID uuid;
    private int id;
    private String name = "";

    private String content;

    private String timeString = DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now());
    private long timeLong = System.currentTimeMillis();

    public LogEntry(LogUUID uuid, int id, String content) {
        this.uuid = uuid;
        this.id = id;
        this.content = content;
    }

    public LogEntry(LogUUID uuid, String name, int id, String content) {
        this.uuid = uuid;
        this.name = name;
        this.id = id;
        this.content = content;
    }

    public LogUUID getUUID() {
        return uuid;
    }

    public String getUUIDString() {
        String s = BaseConversion.toBase64(id);
        return uuid.toString() + "-" + "0".repeat(6 - s.length()) + s + "-" + name;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public long getTime() {
        return timeLong;
    }

    @Override
    public String toString() {
        String uuid = " [" + getUUIDString() + "] ";
        if (!content.contains("\n")) {
            return timeString + uuid + content;
        }
        String[] strings = content.split("\n");
        StringBuilder st = new StringBuilder();
        for (int i = 0; i < strings.length; i++) {
            if (i != 0) {
                st.append("\n");
            }
            if (i == 0) {
                st.append(timeString);
                st.append(uuid);
            } else {
                st.append(" ".repeat(timeString.length()));
                st.append(" ".repeat(uuid.length()));
            }
            st.append(strings[i]);
        }
        return st.toString();
    }

}