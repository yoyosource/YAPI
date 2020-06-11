// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.ui.console;

import org.fusesource.jansi.Ansi;

public class TaskAlignment extends ConsoleMessageTask {

    private ConsoleAlignment consoleAlignment;

    TaskAlignment(ConsoleAlignment alignment) {
        this.consoleAlignment = alignment;
    }

    @Override
    public void runTask(Ansi ansi, Console console) {

    }

    public ConsoleAlignment getConsoleAlignment() {
        return consoleAlignment;
    }

    @Override
    public String toString() {
        return "TaskAlignment{" + consoleAlignment + '}';
    }
}