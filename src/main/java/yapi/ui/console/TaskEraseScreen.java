// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.ui.console;

import org.fusesource.jansi.Ansi;

public class TaskEraseScreen extends ConsoleMessageTask {

    private Ansi.Erase erase;

    public TaskEraseScreen(Ansi.Erase erase) {
        this.erase = erase;
    }

    @Override
    public void runTask(Ansi ansi, Console console) {
        ansi.eraseScreen(erase);
    }

    @Override
    public String toString() {
        return "TaskEraseScreen{" + erase + '}';
    }
}