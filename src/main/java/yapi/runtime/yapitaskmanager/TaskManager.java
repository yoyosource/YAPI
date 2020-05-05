// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.runtime.yapitaskmanager;

import yapi.runtime.ThreadUtils;

import java.util.ArrayList;
import java.util.List;

import static yapi.runtime.ThreadUtils.yapiGroup;

public class TaskManager {

    private static final TaskManager taskManager = new TaskManager();

    public static TaskManager getInstance() {
        return taskManager;
    }

    private String tasks = "";

    private TaskManager() {
        Runnable runnable = () -> {
            while (true) {
                generate();
                ThreadUtils.sleep(500);
            }
        };
        Thread thread = new Thread(yapiGroup, runnable, "TaskManager");
        thread.setDaemon(true);
        thread.start();
    }

    private void generate() {
        int count = yapiGroup.activeGroupCount() + yapiGroup.activeCount();
        Thread[] threads = new Thread[yapiGroup.activeCount() + 10];
        yapiGroup.enumerate(threads, false);
        ThreadGroup[] threadGroups = new ThreadGroup[yapiGroup.activeGroupCount() + 10];
        yapiGroup.enumerate(threadGroups, false);

        tasks = generate(1, yapiGroup, new ArrayList<>()).replace("\n\n", "\n");
    }

    private String generate(int indention, ThreadGroup threadGroup, List<Integer> integers) {
        // `-
        // |

        StringBuilder st = new StringBuilder();
        for (int i = 0; i < indention; i++) {
            if (integers.contains(i)) {
                st.append("   ");
            } else {
                st.append("|  ");
            }
        }
        String ind = st.toString();
        st = new StringBuilder();
        if (ind.length() >= 6) {
            st.append(ind, 0, ind.length() - 6).append("`- ");
        } else {
            st.append(ind, 0, ind.length() - 3);
        }
        st.append(threadGroup.getName()).append("\n");

        ThreadGroup[] threadGroups = new ThreadGroup[threadGroup.activeGroupCount() + 10];
        threadGroup.enumerate(threadGroups, false);
        Thread[] threads = new Thread[threadGroup.activeCount() + 10];
        threadGroup.enumerate(threads, false);

        if (effectiveSize(threads) == 0 && effectiveSize(threadGroups) == 0) {
            integers.add(indention - 1);
        }

        for (int i = 0; i < threadGroups.length; i++) {
            ThreadGroup group = threadGroups[i];
            if (group == null) {
                continue;
            }
            if (group.isDestroyed()) {
                continue;
            }
            if (group.activeCount() == 0) {
                group.destroy();
                continue;
            }
            if (i != 0) {
                st.append("\n");
            }
            if (i == effectiveSize(threadGroups) - 1 && effectiveSize(threads) == 0) {
                integers.add(indention - 1);
            }
            st.append(generate(indention + 1, group, integers));
        }

        if (effectiveSize(threads) == 0) {
            integers.add(indention - 1);
        }

        boolean b = false;
        for (int i = 0; i < threads.length; i++) {
            Thread t = threads[i];
            if (t == null) {
                continue;
            }
            if (i != 0) {
                st.append("\n");
            }
            b = true;
            st.append(ind, 0, ind.length() - 3).append("`- ");
            st.append(t.getName()).append(" [").append(t.getId()).append("]");
        }
        if (b) {
            st.append("\n");
        }

        integers.remove((Integer) indention);
        return st.toString();
    }

    private int effectiveSize(Object[] objects) {
        int i = 0;
        for (Object o : objects) {
            if (o != null) {
                i++;
                if (o instanceof ThreadGroup && ((ThreadGroup) o).isDestroyed()) {
                    i--;
                }
                if (o instanceof Thread && !((Thread) o).isAlive()) {
                    i--;
                }
            }
        }
        return i;
    }

    @Override
    public String toString() {
        return tasks;
    }

}