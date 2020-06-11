// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.ui.console;

import java.util.List;

public interface ConsoleRenderer {

    List<ConsoleMessageTask> render(List<ConsoleMessageTask> tasks, ConsoleClipping clipping, int width);

    default int indent(ConsoleAlignment alignment, int length, int width) {
        switch (alignment) {
            case LEFT:
                return 0;
            case CENTER:
                return width / 2 - length / 2;
            case RIGHT:
                return width - length;
        }
        return 0;
    }

}