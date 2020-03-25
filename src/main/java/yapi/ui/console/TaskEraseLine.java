// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.ui.console;

import org.fusesource.jansi.Ansi;

public class TaskEraseLine extends ConsoleMessageTask {

    private Ansi.Erase erase;

    TaskEraseLine(Ansi.Erase erase) {
        this.erase = erase;
    }

    @Override
    public void runTask(Ansi ansi, Console console) {
        ansi.eraseLine(erase);
    }

    @Override
    public String toString() {
        return "TaskEraseLine{" + erase + '}';
    }
}