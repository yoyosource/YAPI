// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.ui.console;

import java.util.List;

public class ConsoleMessageOverlay extends ConsoleMessage {

    private static ConsoleMessageOverlay getInstance(ConsoleMessage messageToOverlay) {
        ConsoleMessageOverlay consoleMessageOverlay = new ConsoleMessageOverlay(messageToOverlay.getTasks());
        return consoleMessageOverlay;
    }

    private ConsoleMessageOverlay(List<ConsoleMessageTask> tasks) {
        super(tasks);
    }
}