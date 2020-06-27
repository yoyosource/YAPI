// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.ui.console;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ConsoleMessageBuilder {

    private List<ConsoleMessageTask> tasks = new LinkedList<>();

    public ConsoleMessageBuilder() {

    }

    public ConsoleMessageBuilder addTask(ConsoleMessageTask task) {
        tasks.add(task);
        return this;
    }

    public ConsoleMessageBuilder addText(String text) {
        String[] strings = text.split("\n");
        for (int i = 0; i < strings.length; i++) {
            if (i != 0) {
                tasks.add(new TaskNewLine());
            }
            tasks.add(new TaskText(strings[i]));
        }
        return this;
    }

    public ConsoleMessageBuilder addLine() {
        tasks.add(new TaskNewLine());
        return this;
    }

    public ConsoleMessageBuilder newLine() {
        tasks.add(new TaskNewLine());
        return this;
    }

    public ConsoleMessageBuilder eraseLine(ConsoleErase erase) {
        if (!tasks.isEmpty()) {
            throw new IllegalStateException();
        }
        tasks.add(new TaskEraseLine(erase.value()));
        return this;
    }

    public ConsoleMessageBuilder eraseScreen(ConsoleErase erase) {
        if (!tasks.isEmpty()) {
            throw new IllegalStateException();
        }
        tasks.add(new TaskEraseScreen(erase.value()));
        return this;
    }

    public ConsoleMessageBuilder setColor(ConsoleColor color) {
        tasks.add(new TaskColor(color.value()));
        return this;
    }

    public ConsoleMessageBuilder setColor(Color color) {
        tasks.add(new TaskColorRGB(color));
        return this;
    }

    public ConsoleMessageBuilder setColorBright(ConsoleColor color) {
        tasks.add(new TaskColorBright(color.value()));
        return this;
    }

    public ConsoleMessageBuilder setBGColor(ConsoleColor color) {
        tasks.add(new TaskBGColor(color.value()));
        return this;
    }

    public ConsoleMessageBuilder setBGColor(Color color) {
        tasks.add(new TaskBGColorRGB(color));
        return this;
    }

    public ConsoleMessageBuilder setBGColorBright(ConsoleColor color) {
        tasks.add(new TaskBGColorBright(color.value()));
        return this;
    }

    public ConsoleMessageBuilder setAttribute(ConsoleAttribute attribute) {
        tasks.add(new TaskAttribute(attribute.value()));
        return this;
    }

    public ConsoleMessageBuilder setAlignment(ConsoleAlignment alignment) {
        tasks.add(new TaskAlignment(alignment));
        return this;
    }

    public ConsoleMessageBuilder setEscape(String escape) {
        tasks.add(new TaskEscape(escape));
        return this;
    }

    public ConsoleMessageBuilder overLay(int overlaySize) {
        tasks.add(new TaskOverlay(overlaySize));
        return this;
    }

    public ConsoleMessage build() {
        return new ConsoleMessage(tasks);
    }

    public static ConsoleMessage build(String s) {
        List<ConsoleMessageTask> tasks = new ArrayList<>();

        StringBuilder st = new StringBuilder();
        boolean escaped = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (escaped) {
                switch (c) {
                    case 'n':
                        st.append("\n");
                        break;
                    case 'r':
                        st.append("\r");
                        break;
                    case 't':
                        st.append("\t");
                        break;
                    case 'b':
                        st.append("\b");
                        break;
                    default:
                        st.append(c);
                }

                escaped = false;
                continue;
            }

            if (c == '\\') {
                escaped = true;
                continue;
            }

            if (c == '<') {
                if (st.length() > 0) {
                    tasks.add(new TaskText(st.toString()));
                    st = new StringBuilder();
                }
                st.append(c);
            } else if (c == '>') {
                st.append(c);
                String attribute = st.toString();
                st = new StringBuilder();
                if (attribute.length() == 1) {
                    tasks.add(new TaskText(attribute));
                    continue;
                }
                tasks.addAll(buildAttributes(attribute));
            } else {
                if (c == '\n') {
                    tasks.add(new TaskText(st.toString()));
                    st = new StringBuilder();
                    tasks.add(new TaskNewLine());
                    continue;
                }
                st.append(c);
            }
        }
        if (st.length() > 0) {
            tasks.add(new TaskText(st.toString()));
        }

        return new ConsoleMessage(tasks);
    }

    private static List<ConsoleMessageTask> buildAttributes(String s) {
        List<ConsoleMessageTask> tasks = new ArrayList<>();

        String[] strings = s.substring(1, s.length() - 1).split(",");
        for (String t : strings) {
            List<ConsoleMessageTask> ts = ConsoleBuildAttribute.getTask(t);
            if (ts.isEmpty()) {
                tasks = new ArrayList<>();
                tasks.add(new TaskText(s));
                return tasks;
            }
            tasks.addAll(ts);
        }

        return tasks;
    }

}