// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.ui.console;

import java.util.List;

public class ConsoleMessageAppendable extends ConsoleMessage {

    public static ConsoleMessageAppendable getInstance() {
        return new ConsoleMessageAppendable(List.of());
    }

    private ConsoleMessageAppendable(List<ConsoleMessageTask> tasks) {
        super(tasks);
    }

    public ConsoleMessageAppendable appendMessage(ConsoleMessage message) {
        getTasks().addAll(message.getTasks());
        return this;
    }

    public ConsoleMessageAppendable newLine() {
        getTasks().add(new TaskNewLine());
        return this;
    }
    
}