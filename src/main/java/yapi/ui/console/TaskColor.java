// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.ui.console;

import org.fusesource.jansi.Ansi;

public class TaskColor extends ConsoleMessageTask {

    private Ansi.Color color;

    TaskColor(Ansi.Color color) {
        this.color = color;
    }

    @Override
    public void runTask(Ansi ansi, Console console) {
        ansi.fg(color);
    }

    @Override
    public String toString() {
        return "TaskColor{" + color + '}';
    }
}