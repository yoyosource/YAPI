// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.ui.console;

import org.fusesource.jansi.Ansi;

class TaskIndention extends ConsoleMessageTask {

    private int indention;

    TaskIndention(int indention) {
        this.indention = indention;
    }

    @Override
    void runTask(Ansi ansi, Console console) {
        if (indention <= 0) {
            return;
        }
        ansi.a(" ".repeat(indention));
    }

}