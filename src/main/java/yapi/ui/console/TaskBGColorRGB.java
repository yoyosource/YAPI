// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.ui.console;

import org.fusesource.jansi.Ansi;

import java.awt.*;

public class TaskBGColorRGB extends ConsoleMessageTask {

    private static final char FIRST_ESC_CHAR = 27;
    private static final char SECOND_ESC_CHAR = '[';

    private Color color;

    TaskBGColorRGB(Color color) {
        this.color = color;
    }

    @Override
    void runTask(Ansi ansi, Console console) {
        ansi.a("" + FIRST_ESC_CHAR + SECOND_ESC_CHAR + (char)38 + ";" + color.getRed() + ";" + color.getGreen() + ";" + color.getBlue() + "m");
    }

}