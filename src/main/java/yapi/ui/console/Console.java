// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.ui.console;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;
import yapi.runtime.TerminalUtils;

import java.util.ArrayList;
import java.util.List;

public class Console {

    public static void main(String[] args) {
        Console console = new Console();

        console.send(ConsoleMessageBuilder.build("<RED>\\\\\\\\  <RED:BRIGHT,BOLD>\\\\¸/<WHITE> /¯\\\\ |¯) ˙|˙</BOLD>  <RED>//"));
        console.send(ConsoleMessageBuilder.build("<RED>//  <RED:BRIGHT,BOLD> |<WHITE>  |¯| |¯  ¸|¸</BOLD>  <RED>\\\\\\\\"));
        console.send(ConsoleMessageBuilder.build(""));
        console.send(ConsoleMessageBuilder.build("<WHITE:BRIGHT>(C)<WHITE> 2019,2020 <RED:BRIGHT>y<WHITE>oyosource Apache-2.0"));
    }

    private ConsoleAlignment alignment = ConsoleAlignment.LEFT;
    private ConsoleClipping clipping = ConsoleClipping.NO_CLIP;

    public Console() {
        AnsiConsole.systemInstall();
    }

    public Console(ConsoleClipping clipping) {
        this.clipping = clipping;
        AnsiConsole.systemInstall();
    }

    public synchronized void send(ConsoleMessage message) {
        List<ConsoleMessageTask> tasks = render(message.getTasks());
        Ansi ansi = Ansi.ansi();

        for (ConsoleMessageTask task : tasks) {
            task.runTask(ansi, this);
        }

        ansi.reset();
        alignment = ConsoleAlignment.LEFT;
        System.out.println(ansi.toString());
    }

    private ConsoleMessageAll split(List<ConsoleMessageTask> tasks) {
        ConsoleMessageAll consoleMessageAll = new ConsoleMessageAll();

        for (ConsoleMessageTask task : tasks) {
            consoleMessageAll.add(task);
        }

        return consoleMessageAll;
    }

    private List<ConsoleMessageTask> render(List<ConsoleMessageTask> tasks) {
        List<ConsoleMessageSnippet> snippets = split(tasks).getSnippets();
        int width = getWidth();

        if (snippets.isEmpty()) {
            return new ArrayList<>();
        }

        List<ConsoleMessageTask> consoleMessageTasks = new ArrayList<>();
        if (snippets.get(0).getEraseTask() != null) {
            consoleMessageTasks.add(snippets.get(0).getEraseTask());
        }

        consoleMessageTasks.add(new InternalTaskIndention(indent(snippets.get(0).getAlgn(), snippets.get(0).getLength(), width)));
        consoleMessageTasks.addAll(snippets.get(0).getTasks());

        for (int i = 1; i < snippets.size(); i++) {
            ConsoleMessageSnippet snippetPrevious = snippets.get(i - 1);
            int previousIndention = indent(snippetPrevious.getAlgn(), snippetPrevious.getLength(), width) + snippetPrevious.getLength();

            ConsoleMessageSnippet snippet = snippets.get(i);
            int currentIndention = indent(snippet.getAlgn(), snippet.getLength(), width);

            if (previousIndention > currentIndention) {
                consoleMessageTasks.add(new TaskNewLine());
                consoleMessageTasks.add(new InternalTaskIndention(currentIndention));
                consoleMessageTasks.addAll(snippet.getTasks());
            } else {
                consoleMessageTasks.add(new InternalTaskIndention(currentIndention - previousIndention));
                consoleMessageTasks.addAll(snippet.getTasks());
            }
        }

        return consoleMessageTasks;
    }

    private int indent(ConsoleAlignment alignment, int length, int width) {
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

    public int getWidth() {
        return TerminalUtils.getWidth();
    }

    public int getHeight() {
        return TerminalUtils.getHeight();
    }

    void setAlignment(ConsoleAlignment alignment) {
        this.alignment = alignment;
    }

    public void setClipping(ConsoleClipping clipping) {
        this.clipping = clipping;
    }

    private class ConsoleMessageAll {

        private List<ConsoleMessageLine> lines = new ArrayList<>();

        private ConsoleAlignment lastAlignment = alignment;

        private int width = getWidth();

        public ConsoleMessageAll() {
            lines.add(new ConsoleMessageLine());
        }

        public void add(ConsoleMessageTask task) {
            if (task instanceof TaskAlignment) {
                ConsoleAlignment algn = ((TaskAlignment) task).getConsoleAlignment();
                if (!lastAlignment.equals(algn) && illegalAlignmentChange(algn)) {
                    lines.add(new ConsoleMessageLine());
                }
                lastAlignment = algn;
            }
            if (task instanceof TaskNewLine) {
                lines.add(new ConsoleMessageLine());
                lines.get(lines.size() - 1).add(new TaskAlignment(lastAlignment));
                return;
            }

            if (task instanceof TaskText) {
                String text = ((TaskText) task).getText();

                if (text.length() <= width) {
                    addText(text, false);
                    return;
                }

                if (clipping.equals(ConsoleClipping.WRAP)) {
                    String[] words = ((TaskText) task).getText().split(" ");
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
                } else if (clipping.equals(ConsoleClipping.NO_CLIP)) {
                    while (text.length() > width) {
                        String s = text.substring(0, width);
                        text = text.substring(width);
                        addText(s, true);
                    }

                    if (text.length() > 0) {
                        addText(text, true);
                    }
                }
            } else {
                lines.get(lines.size() - 1).add(task);
            }
        }

        private void addText(String text, boolean nLine) {
            if (nLine) {
                lines.add(new ConsoleMessageLine());
                lines.get(lines.size() - 1).add(new TaskAlignment(lastAlignment));
                lines.get(lines.size() - 1).add(new TaskText(text));
            } else {
                lines.get(lines.size() - 1).add(new TaskText(text));
            }
        }

        public List<ConsoleMessageSnippet> getSnippets() {
            List<ConsoleMessageSnippet> snippets = new ArrayList<>();
            for (int i = 0; i < lines.size(); i++) {
                if (i != 0) {
                    snippets.add(new ConsoleMessageSnippet(true));
                }
                ConsoleMessageLine consoleMessageLine = lines.get(i);
                snippets.addAll(consoleMessageLine.getSnippets());
            }
            return snippets;
        }

        @Override
        public String toString() {
            return "ConsoleMessageAll{" +
                    "lines=" + lines +
                    '}';
        }

        private boolean illegalAlignmentChange(ConsoleAlignment alignment) {
            if (alignment.equals(ConsoleAlignment.LEFT) && (lastAlignment.equals(ConsoleAlignment.CENTER) || lastAlignment.equals(ConsoleAlignment.RIGHT))) {
                return true;
            }
            return alignment.equals(ConsoleAlignment.CENTER) && (lastAlignment.equals(ConsoleAlignment.RIGHT));
        }

    }

    private class ConsoleMessageLine {

        private List<ConsoleMessageSnippet> snippets = new ArrayList<>();

        public ConsoleMessageLine() {
            snippets.add(new ConsoleMessageSnippet());
        }

        public void add(ConsoleMessageTask task) {
            if (task instanceof TaskAlignment) {
                if (!snippets.get(snippets.size() - 1).isEmpty()) {
                    snippets.add(new ConsoleMessageSnippet());
                }
            }
            snippets.get(snippets.size() - 1).add(task);
        }

        public boolean isEmpty() {
            return snippets.isEmpty();
        }

        public List<ConsoleMessageSnippet> getSnippets() {
            return snippets;
        }

        @Override
        public String toString() {
            return "ConsoleMessageLine{" +
                    "snippets=" + snippets +
                    '}';
        }

    }

    private class ConsoleMessageSnippet {

        private List<ConsoleMessageTask> tasks = new ArrayList<>();
        private ConsoleAlignment algn = alignment;
        private ConsoleMessageTask eraseTask = null;
        private boolean newLine = false;

        private int length = 0;

        public ConsoleMessageSnippet() {

        }

        public ConsoleMessageSnippet(boolean newLine) {
            this.newLine = newLine;
        }

        public void add(ConsoleMessageTask task) {
            if (newLine) {
                throw new IllegalStateException();
            }
            if (task instanceof TaskAlignment) {
                algn = ((TaskAlignment) task).getConsoleAlignment();
            }
            if (task instanceof TaskEraseScreen || task instanceof TaskEraseLine) {
                eraseTask = task;
                return;
            }
            if (task instanceof TaskText) {
                length += ((TaskText) task).getText().length();
            }
            tasks.add(task);
        }

        public boolean isEmpty() {
            return tasks.isEmpty();
        }

        public List<ConsoleMessageTask> getTasks() {
            return tasks;
        }

        public ConsoleAlignment getAlgn() {
            return algn;
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

        @Override
        public String toString() {
            if (newLine) {
                return "ConsoleMessageSnippet{newLine=" + newLine + "}";
            }
            return "ConsoleMessageSnippet{" +
                    "alignment=" + algn +
                    ", size=" + length +
                    ", tasks=" + tasks +
                    '}';
        }

    }

}