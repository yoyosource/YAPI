// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.ui.console;

class ConsoleMessageSendStats {

    int previousTasks = 0;
    int optimizedTasks = 0;

    long renderTime = 0;

    @Override
    public String toString() {
        return "ConsoleMessageSendStats{" +
                "previousTasks=" + previousTasks +
                ", optimizedTasks=" + optimizedTasks +
                ", renderTime=" + renderTime +
                '}';
    }

}