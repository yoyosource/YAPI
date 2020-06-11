// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.ui.console;

import java.util.ArrayList;
import java.util.List;

public class CRenderAll {

    RenderController controller;

    List<CRenderLine> lines = new ArrayList<>();
    ConsoleAlignment lastAlignment;
    int currentWidth = 0;
    int width = 0;

    public CRenderAll(RenderController controller) {
        this.controller = controller;
        this.lastAlignment = controller.getAlignment();
        this.width = controller.getWidth();
        lines.add(new CRenderLine(controller));
    }

    public void add(ConsoleMessageTask task) {
        controller.add(task, this);
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