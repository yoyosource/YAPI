// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.ui.console;

import org.fusesource.jansi.Ansi;

public class TaskAttribute extends ConsoleMessageTask {

    private Ansi.Attribute attribute;

    TaskAttribute(Ansi.Attribute attribute) {
        this.attribute = attribute;
    }

    @Override
    public void runTask(Ansi ansi, Console console) {
        if (attribute.equals(Ansi.Attribute.RESET)) {
            console.setClipping(ConsoleClipping.WRAP_OFF);
        }
        ansi.a(attribute);
    }

    @Override
    public String toString() {
        return "TaskAttribute{" + attribute + '}';
    }
}