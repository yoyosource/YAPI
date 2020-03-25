// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.ui.console;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConsoleMessage {

    private List<ConsoleMessageTask> tasks = new ArrayList<>();

    ConsoleMessage(List<ConsoleMessageTask> tasks) {
        Collections.addAll(this.tasks, tasks.toArray(new ConsoleMessageTask[0]));
    }

    List<ConsoleMessageTask> getTasks() {
        return tasks;
    }

    @Override
    public String toString() {
        return "ConsoleMessage{" +
                "tasks=" + tasks +
                '}';
    }

}