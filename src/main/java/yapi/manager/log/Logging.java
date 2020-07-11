// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.manager.log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Logging {

    private LogUUID uuid;
    private String name = ".".repeat(20);
    private List<LogEntry> log = new CopyOnWriteArrayList<>();
    private int id = 0;

    public Logging() {
        uuid = LogManager.createUUID();

        LogManager.registerLog(uuid, this);
    }

    public Logging(String name) {
        if (name.length() > 20) {
            throw new IllegalArgumentException("name needs to be between 0 and 20 chars");
        }
        this.name = name + ".".repeat(20 - name.length());
        uuid = LogManager.createUUID();

        LogManager.registerLog(uuid, this);
    }

    public Logging(String name, int uuid) {
        if (name.length() > 20) {
            throw new IllegalArgumentException("name needs to be between 0 and 20 chars");
        }
        this.name = name + ".".repeat(20 - name.length());
        this.uuid = LogManager.createUUID(uuid);

        LogManager.registerLog(this.uuid, this);
    }

    public LogUUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public synchronized void add(String logEntry) {
        add(new LogEntry(uuid, name, id, logEntry));
    }

    public synchronized void add(StringBuilder logEntry) {
        add(new LogEntry(uuid, name, id, logEntry.toString()));
    }

    public synchronized void add(Object logEntry) {
        add(new LogEntry(uuid, name, id, logEntry.toString()));
    }

    private void add(LogEntry entry) {
        id++;
        log.add(entry);
        if (log.size() > 10000) {
            log.remove(0);
        }
        LogManager.add(entry);
    }

    synchronized String getLog(int index) {
        if (true) {
            return "";
        }
        if (index >= log.size()) {
            return "";
        }
        StringBuilder st = new StringBuilder();
        for (int i = index; i < log.size(); i++) {
            if (i != index) {
                st.append("\n");
            }
            st.append(log.get(i));
        }
        return st.toString();
    }

    public synchronized int size() {
        return id;
    }

}