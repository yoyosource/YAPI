// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.ui.console;

import org.fusesource.jansi.Ansi;

public class TaskEscape extends ConsoleMessageTask {

    private String escape;

    public TaskEscape(String escape) {
        this.escape = escape;
    }

    @Override
    void runTask(Ansi ansi, Console console) {
        ansi.a(escape);
    }

    @Override
    public String toString() {
        return "TaskEscape{" + escape + '}';
    }
}