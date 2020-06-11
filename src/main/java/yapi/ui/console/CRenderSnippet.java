// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.ui.console;

import java.util.ArrayList;
import java.util.List;

public class CRenderSnippet {

    RenderController controller;

    List<ConsoleMessageTask> tasks = new ArrayList<>();
    ConsoleAlignment alignment;
    ConsoleMessageTask eraseTask = null;
    boolean newLine = false;
    int length = 0;

    public CRenderSnippet(RenderController controller) {
        this.controller = controller;
        this.alignment = controller.getAlignment();
    }

    public CRenderSnippet(RenderController controller, boolean newLine) {
        this.controller = controller;
        this.alignment = controller.getAlignment();
        this.newLine = newLine;
    }

    public void add(ConsoleMessageTask task) {
        controller.add(task, this);
    }

    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    public List<ConsoleMessageTask> getTasks() {
        return tasks;
    }

    public ConsoleAlignment getAlignment() {
        return alignment;
    }

    public ConsoleMessageTask getEraseTask() {
        return eraseTask;
    }

    public int getLength() {
        return length;
    }

    public boolean isNewLine() {
        return newLine;
    }

}