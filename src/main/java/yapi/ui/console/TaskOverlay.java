// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.ui.console;

import org.fusesource.jansi.Ansi;

public class TaskOverlay extends ConsoleMessageTask {

    private static final String overlayText = "<OVERLAY> ";
    private static int index = 0;
    private final int overlaySize;

    public TaskOverlay(int overlaySize) {
        this.overlaySize = overlaySize;
    }

    @Override
    void runTask(Ansi ansi, Console console) {
        StringBuilder st = new StringBuilder();
        for (int i = 0; i < overlaySize; i++) {
            st.append(overlayText.charAt(index));
            index = (index + 1) % overlayText.length();
        }
        ansi.a(st.toString());
    }

    @Override
    public String toString() {
        return "TaskOverlay{" + index + ":" + overlaySize + '}';
    }

}