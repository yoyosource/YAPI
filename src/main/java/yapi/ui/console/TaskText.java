// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.ui.console;

import org.fusesource.jansi.Ansi;

public class TaskText extends ConsoleMessageTask {

    private String text;

    TaskText(String text) {
        this.text = text;
    }

    @Override
    public void runTask(Ansi ansi, Console console) {
        ansi.a(text);
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "TaskText{'" + text + "'}";
    }
}