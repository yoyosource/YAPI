// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.ui.console;

import org.fusesource.jansi.Ansi;

public class TaskNewLine extends ConsoleMessageTask {

    @Override
    void runTask(Ansi ansi, Console console) {
        ansi.newline();
    }

    @Override
    public String toString() {
        return "TaskNewLine{}";
    }

}