// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.ui.console;

import org.fusesource.jansi.Ansi;

public class TaskColorBright extends ConsoleMessageTask {

    private Ansi.Color color;

    TaskColorBright(Ansi.Color color) {
        this.color = color;
    }

    Ansi.Color getColor() {
        return color;
    }

    @Override
    public void runTask(Ansi ansi, Console console) {
        ansi.fgBright(color);
    }

    @Override
    public String toString() {
        return "TaskColorBright{" + color + '}';
    }

}