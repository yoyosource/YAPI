// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.ui.console;

import org.fusesource.jansi.Ansi;

import java.util.ArrayList;
import java.util.List;

public class CRenderAll {

    RenderController controller;

    List<CRenderLine> lines = new ArrayList<>();
    ConsoleAlignment lastAlignment;
    int currentWidth = 0;
    int width = 0;

    private Ansi.Color currentColor = null;
    private boolean isBright = false;
    private Ansi.Color currentColorBackground = null;
    private boolean isBrightBackground = false;
    private Ansi.Attribute currentAttribute = null;

    private StringBuilder st = null;

    public CRenderAll(RenderController controller) {
        this.controller = controller;
        this.lastAlignment = controller.getAlignment();
        this.width = controller.getWidth();
        lines.add(new CRenderLine(controller));
    }

    public void add(ConsoleMessageTask task) {
        if (task instanceof TaskColor) {
            if (currentColor != null && currentColor == ((TaskColor) task).getColor() && !isBright) return;
            currentColor = ((TaskColor) task).getColor();
            isBright = false;
        }
        if (task instanceof TaskColorBright) {
            if (currentColor != null && currentColor == ((TaskColorBright) task).getColor() && isBright) return;
            currentColor = ((TaskColorBright) task).getColor();
            isBright = true;
        }
        if (task instanceof TaskBGColor) {
            if (currentColorBackground != null && currentColorBackground == ((TaskBGColor) task).getColor() && !isBrightBackground) return;
            currentColorBackground = ((TaskBGColor) task).getColor();
            isBrightBackground = false;
        }
        if (task instanceof TaskBGColorBright) {
            if (currentColorBackground != null && currentColorBackground == ((TaskBGColorBright) task).getColor() && isBrightBackground) return;
            currentColorBackground = ((TaskBGColorBright) task).getColor();
            isBrightBackground = true;
        }
        if (task instanceof TaskAttribute) {
            if (currentAttribute != null && currentAttribute == ((TaskAttribute) task).getAttribute()) return;
            currentAttribute = ((TaskAttribute) task).getAttribute();
        }
        if (task instanceof TaskText) {
            textCase((TaskText) task);
            return;
        } else if (st != null) {
            controller.add(new TaskText(st.toString()), this);
            st = null;
        }
        controller.add(task, this);
    }

    private void textCase(TaskText taskText) {
        if (st == null) st = new StringBuilder();
        String text = taskText.getText();

        if (!text.contains("\n")) {
            st.append(text);
            return;
        }

        int length = text.length();
        for (int i = 0; i < length; i++) {
            char c = text.charAt(i);
            if (c != '\n' && c != '\t') {
                st.append(c);
                continue;
            }
            controller.add(new TaskText(st.toString()), this);
            st = new StringBuilder();
        }
        if (text.charAt(length - 1) == '\n' || text.charAt(length - 1) == '\t') st = null;
    }

    void wrapHard(String text) {
        while (text.length() > width) {
            String s = text.substring(0, width);
            text = text.substring(width);
            addText(s, true);
        }

        addText(text, true);
    }

    void wrapSoft(String text) {
        String[] words = text.split(" ");
        StringBuilder st = new StringBuilder();

        for (int i = 0; i < words.length; i++) {
            if (st.length() + words[i].length() > width) {
                addText(st.toString(), true);
                st = new StringBuilder();

                if (words[i].length() > width) {
                    String s = words[i];
                    while (s.length() > width) {
                        addText(s.substring(0, width), true);
                        s = s.substring(width);
                    }
                    if (s.length() > 0) {
                        st.append(s);
                    }
                }
            } else {
                if (i != 0) {
                    st.append(" ");
                }
                st.append(words[i]);
            }
        }

        if (st.length() > 0) {
            addText(st.toString(), true);
        }
    }

    void wrapOff(String text) {
        addText(text, false);
    }

    void clipWidth(String text) {
        addText(text.substring(0, width - currentWidth), false);
    }

    void addText(String text, boolean nLine) {
        currentWidth += text.length();
        if (nLine) {
            currentWidth = 0;
            lines.add(new CRenderLine(controller));
            lines.get(lines.size() - 1).add(new TaskAlignment(lastAlignment));
        }
        lines.get(lines.size() - 1).add(new TaskText(text));
    }

    public List<CRenderSnippet> getSnippets() {
        List<CRenderSnippet> snippets = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            if (i != 0) {
                snippets.add(new CRenderSnippet(controller, true));
            }
            CRenderLine consoleMessageLine = lines.get(i);
            snippets.addAll(consoleMessageLine.getSnippets());
        }
        return snippets;
    }

    boolean illegalAlignmentChange(ConsoleAlignment alignment) {
        if (alignment.equals(ConsoleAlignment.LEFT) && (lastAlignment.equals(ConsoleAlignment.CENTER) || lastAlignment.equals(ConsoleAlignment.RIGHT))) {
            return true;
        }
        return alignment.equals(ConsoleAlignment.CENTER) && (lastAlignment.equals(ConsoleAlignment.RIGHT));
    }

}