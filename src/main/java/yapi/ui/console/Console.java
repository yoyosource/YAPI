// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.ui.console;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;
import yapi.runtime.TerminalUtils;
import yapion.annotations.deserialize.YAPIONLoad;
import yapion.annotations.serialize.YAPIONSave;

import java.util.List;

@YAPIONSave
@YAPIONLoad
public class Console {

    public static void main(String[] args) {
        Console console = new Console();
        console.send(ConsoleMessageBuilder.build("<RED>\\\\\\\\  <RED:BRIGHT,BOLD>\\\\¸/<WHITE> /¯\\\\ |¯) ˙|˙</BOLD>  <RED>//"));
        console.send(ConsoleMessageBuilder.build("<RED>//  <RED:BRIGHT,BOLD> |<WHITE>  |¯| |¯  ¸|¸</BOLD>  <RED>\\\\\\\\"));
        console.send(ConsoleMessageBuilder.build(""));
        console.send(ConsoleMessageBuilder.build("<WHITE:BRIGHT>(C)<WHITE> 2019,2020 <RED:BRIGHT>y<WHITE>oyosource Apache-2.0"));
    }

    private ConsoleClipping clipping = ConsoleClipping.WRAP_OFF;
    private ConsoleRenderer renderer = new RendererDefault();

    private ConsoleMessageSendStats stats = null;

    public Console() {
        AnsiConsole.systemInstall();
    }

    public Console(ConsoleClipping clipping) {
        this.clipping = clipping;
        AnsiConsole.systemInstall();
    }

    public synchronized Console setRenderer(boolean alignment, boolean clipping) {
        if (alignment && clipping) {
            renderer = new RendererDefault();
        }
        if (!alignment && clipping) {
            renderer = new RendererWithoutAlignment();
        }
        if (alignment && !clipping) {
            renderer = new RendererWithoutClipping();
        }
        if (!alignment && !clipping) {
            renderer = new RendererWithoutAlignmentAndClipping();
        }
        return this;
    }

    public synchronized Console setClipping(ConsoleClipping clipping) {
        this.clipping = clipping;
        return this;
    }

    public synchronized Console wrapSoft() {
        clipping = ConsoleClipping.WRAP_SOFT;
        return this;
    }

    public synchronized Console wrapHard() {
        clipping = ConsoleClipping.WRAP_HARD;
        return this;
    }

    public synchronized Console wrapOff() {
        clipping = ConsoleClipping.WRAP_OFF;
        return this;
    }

    public synchronized Console clipWidth() {
        clipping = ConsoleClipping.CLIP_WIDTH;
        return this;
    }

    public synchronized Console send(ConsoleMessage message) {
        ConsoleMessageSendStats stats = new ConsoleMessageSendStats();
        stats.previousTasks = message.getTasks().size();
        long time = System.currentTimeMillis();
        List<ConsoleMessageTask> tasks = renderer.render(message.getTasks(), clipping, getWidth());
        time = System.currentTimeMillis() - time;
        stats.optimizedTasks = tasks.size();
        stats.renderTime = time;
        this.stats = stats;
        Ansi ansi = Ansi.ansi();

        for (ConsoleMessageTask task : tasks) {
            task.runTask(ansi, this);
        }

        ansi.reset();
        System.out.println(ansi.toString());
        return this;
    }

    public int getWidth() {
        return TerminalUtils.getWidth();
    }

    public int getHeight() {
        return TerminalUtils.getHeight();
    }

    public ConsoleMessageSendStats getStats() {
        return stats;
    }

}