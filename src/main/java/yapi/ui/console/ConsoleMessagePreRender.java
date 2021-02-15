// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.ui.console;

import java.util.LinkedList;
import java.util.List;

public class ConsoleMessagePreRender extends ConsoleMessage {

    public static ConsoleMessagePreRender getInstance(Console console) {
        ConsoleMessagePreRender messagePreRender = new ConsoleMessagePreRender(List.of());
        messagePreRender.width = console.getWidth();
        messagePreRender.clipping = console.getClipping();
        messagePreRender.init();
        return messagePreRender;
    }

    private int width = 0;
    private final ConsoleAlignment alignment = ConsoleAlignment.LEFT;
    private ConsoleClipping clipping = ConsoleClipping.WRAP_OFF;

    private RenderController controller;
    List<ConsoleMessageTask> preRendered = new LinkedList<>();
    private CRenderAll cRenderAll;

    private ConsoleMessagePreRender(List<ConsoleMessageTask> tasks) {
        super(tasks);
    }

    private void init() {
        controller = new RenderController() {
            @Override
            public void add(ConsoleMessageTask task, CRenderAll renderAll) {
                if (task instanceof TaskAlignment) {
                    ConsoleAlignment consoleAlignment = ((TaskAlignment) task).getConsoleAlignment();
                    if (!renderAll.lastAlignment.equals(consoleAlignment) && renderAll.illegalAlignmentChange(consoleAlignment)) {
                        renderAll.currentWidth = 0;
                        renderAll.lines.add(new CRenderLine(controller));
                    }
                    renderAll.lastAlignment = consoleAlignment;
                }
                if (task instanceof TaskNewLine) {
                    renderAll.currentWidth = 0;
                    renderAll.lines.add(new CRenderLine(controller));
                    renderAll.lines.get(renderAll.lines.size() - 1).add(new TaskAlignment(renderAll.lastAlignment));
                    return;
                }

                if (task instanceof TaskText) {
                    String text = ((TaskText) task).getText();

                    if (renderAll.currentWidth + text.length() <= width) {
                        renderAll.addText(text, false);
                        return;
                    }

                    if (clipping.equals(ConsoleClipping.WRAP_HARD)) {
                        renderAll.wrapHard(text);
                    } else if (clipping.equals(ConsoleClipping.WRAP_SOFT)) {
                        renderAll.wrapSoft(text);
                    } else if (clipping.equals(ConsoleClipping.WRAP_OFF)) {
                        renderAll.wrapOff(text);
                    } else if (clipping.equals(ConsoleClipping.CLIP_WIDTH)) {
                        renderAll.clipWidth(text);
                    }
                } else {
                    renderAll.lines.get(renderAll.lines.size() - 1).add(task);
                }
            }

            @Override
            public void add(ConsoleMessageTask task, CRenderLine renderLine) {
                if (task instanceof TaskAlignment && !renderLine.snippets.get(renderLine.snippets.size() - 1).isEmpty()) {
                    renderLine.snippets.add(new CRenderSnippet(controller));
                }
                renderLine.snippets.get(renderLine.snippets.size() - 1).add(task);
            }

            @Override
            public void add(ConsoleMessageTask task, CRenderSnippet renderSnippet) {
                if (renderSnippet.newLine) {
                    throw new IllegalStateException();
                }
                if (task instanceof TaskAlignment) {
                    renderSnippet.alignment = ((TaskAlignment) task).getConsoleAlignment();
                }
                if (task instanceof TaskEraseScreen || task instanceof TaskEraseLine) {
                    renderSnippet.eraseTask = task;
                    return;
                }
                if (task instanceof TaskText) {
                    renderSnippet.length += ((TaskText) task).getText().length();
                }
                renderSnippet.tasks.add(task);
            }

            @Override
            public ConsoleAlignment getAlignment() {
                return alignment;
            }

            @Override
            public ConsoleClipping getClipping() {
                return clipping;
            }

            @Override
            public int getWidth() {
                return width;
            }
        };
        cRenderAll = new CRenderAll(controller);
    }

    public synchronized ConsoleMessagePreRender appendMessage(ConsoleMessage message) {
        getTasks().addAll(message.getTasks());
        for (ConsoleMessageTask task : message.getTasks()) {
            cRenderAll.add(task);
        }
        return this;
    }

    public ConsoleMessagePreRender newLine() {
        getTasks().add(new TaskNewLine());
        cRenderAll.add(new TaskNewLine());
        return this;
    }

    boolean validRender(Console console) {
        return console.getWidth() == width && console.getClipping() == clipping;
    }

    List<ConsoleMessageTask> getPreRenderedTasks() {
        preRendered = finishRender(cRenderAll);
        return preRendered;
    }

    private List<ConsoleMessageTask> finishRender(CRenderAll cRenderAll) {
        List<CRenderSnippet> snippets = cRenderAll.getSnippets();

        if (snippets.isEmpty()) {
            return new LinkedList<>();
        }

        List<ConsoleMessageTask> consoleMessageTasks = new LinkedList<>();
        if (snippets.get(0).getEraseTask() != null) {
            consoleMessageTasks.add(snippets.get(0).getEraseTask());
        }

        int indentation = indent(snippets.get(0).getAlignment(), snippets.get(0).getLength(), width);
        if (indentation != 0) {
            consoleMessageTasks.add(new TaskIndention(indentation));
        }
        consoleMessageTasks.addAll(snippets.get(0).getTasks());

        for (int i = 1; i < snippets.size(); i++) {
            CRenderSnippet snippetPrevious = snippets.get(i - 1);
            int previousIndention = indent(snippetPrevious.getAlignment(), snippetPrevious.getLength(), width) + snippetPrevious.getLength();

            CRenderSnippet snippet = snippets.get(i);
            int currentIndention = indent(snippet.getAlignment(), snippet.getLength(), width);

            if (previousIndention > currentIndention) {
                consoleMessageTasks.add(new TaskNewLine());
            }
            consoleMessageTasks.add(new TaskIndention(currentIndention - previousIndention));
            consoleMessageTasks.addAll(snippet.getTasks());
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

}