// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.ui.console;

import org.fusesource.jansi.Ansi;

public class TaskBGColor extends ConsoleMessageTask {

    private Ansi.Color color;

    TaskBGColor(Ansi.Color color) {
        this.color = color;
    }

    @Override
    public void runTask(Ansi ansi, Console console) {
        ansi.bg(color);
    }

    @Override
    public String toString() {
        return "TaskBGColor{" + color + '}';
    }
}