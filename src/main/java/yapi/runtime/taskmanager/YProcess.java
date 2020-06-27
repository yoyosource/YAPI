// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.runtime.taskmanager;

import org.jutils.jprocesses.JProcesses;
import org.jutils.jprocesses.model.JProcessesResponse;
import org.jutils.jprocesses.model.ProcessInfo;
import yapi.comparator.complex.*;
import yapi.file.FileUtils;
import yapi.math.NumberUtils;
import yapi.runtime.RunningProcess;
import yapi.runtime.ThreadUtils;
import yapi.string.StringFormatting;
import yapi.ui.console.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class YProcess {

    public static void main(String[] args) {
        Console console = new Console();
        // false, false -> ConsoleMessageSendStats{previousTasks=6390, optimizedTasks=7456, renderTime=127}
        // false, true  -> ConsoleMessageSendStats{previousTasks=6354, optimizedTasks=7414, renderTime=171}
        // true, false  -> ConsoleMessageSendStats{previousTasks=6390, optimizedTasks=7456, renderTime=138}
        // true, true   -> ConsoleMessageSendStats{previousTasks=6372, optimizedTasks=7435, renderTime=110}
        console.setRenderer(true, true);
        console.setClipping(ConsoleClipping.CLIP_WIDTH);

        //ConsoleMessageAppendable consoleMessageAppendable = ConsoleMessageAppendable.getInstance();
        ConsoleMessagePreRender consoleMessageAppendable = ConsoleMessagePreRender.getInstance(console);
        ComparatorManager<YProcess> yProcessComparatorManager = new ComparatorManager<>(new ComparatorList<>(
                //new Compare<>(YProcess::getCpuUsage, Double::compare),
                new ComparatorBranch<>(
                        new Compare<>(YProcess::getCpuUsage, Double::compare),
                        new ComparatorListStaticNegative<>(),
                        new ComparatorListStaticEqual<>(),
                        new ComparatorListStaticPositive<>()
                ),
                //new Compare<>(YProcess::getPhysicalMemory, Long::compare),
                //new Compare<>(YProcess::getVirtualMemory, Long::compare),
                //new Compare<>(YProcess::getUser, StringComparators.compareString),
                //new Compare<>(YProcess::getCommand, StringComparators.compareString),
                new Compare<>(YProcess::getPID, Integer::compare)
        ));
        ProcessUtils.getYProcesses().stream().map(YProcess::toCompactColorString).forEach(message -> consoleMessageAppendable.appendMessage(message).newLine());
        System.out.println(YProcess.legendString());
        console.send(consoleMessageAppendable);
        System.out.println(console.getStats());
        //System.out.println(ProcessUtils.getYProcesses().stream().filter(o -> o.getUser().equals("jojo")).map(YProcess::toCompactString).collect(Collectors.joining("\n")));
    }

    private static Thread updater;
    private static List<YProcess> allProcesses = new LinkedList<>();

    private static void createUpdater() {
        if (updater != null) {
            return;
        }

        updater = new Thread(ThreadUtils.yapiGroup, () -> {
            while (true) {
                List<ProcessInfo> processInfos = ProcessUtils.getProcesses();
                List<Integer> pids = createPIDList(processInfos);

                for (int i = allProcesses.size() - 1; i >= 0; i--) {
                    YProcess process = allProcesses.get(i);
                    if (!pids.contains(process.getPID())) {
                        allProcesses.remove(i);
                        continue;
                    }
                    if (!processInfos.get(pids.indexOf(process.getPID())).getCommand().equals(allProcesses.get(i).getCommand())) {
                        allProcesses.remove(i);
                        continue;
                    }
                    process.assemble(processInfos.get(pids.indexOf(process.getPID())));
                }
                ThreadUtils.sleep(50);
            }
        });
        updater.setDaemon(true);
        updater.setName("YProcess UpdaterThread");
        updater.start();
    }

    private static List<Integer> createPIDList(List<ProcessInfo> processInfos) {
        List<Integer> pids = new LinkedList<>();
        for (ProcessInfo info : processInfos) {
            pids.add(Integer.parseInt(info.getPid()));
        }
        return pids;
    }

    private String name;
    private String command;
    private String user;

    private double cpuUsage;
    private long physicalMemory;
    private long virtualMemory;

    private int pid;
    private int priority;

    private String time;
    private String startTime;

    public static YProcess getInstance(ProcessInfo processInfo) {
        for (YProcess process : allProcesses) {
            if (process.getPID() == Integer.parseInt(processInfo.getPid())) return process;
        }
        return new YProcess(processInfo);
    }

    public static YProcess getInstance(RunningProcess runningProcess) {
        for (YProcess process : allProcesses) {
            if (process.getPID() == (int)runningProcess.getPID()) return process;
        }
        return new YProcess(runningProcess);
    }

    private YProcess(ProcessInfo processInfo) {
        assemble(processInfo);
        allProcesses.add(this);
        createUpdater();
    }

    private YProcess(RunningProcess runningProcess) {
        assemble(runningProcess);
        allProcesses.add(this);
        createUpdater();
    }

    private void assemble(RunningProcess runningProcess) {
        assemble(ProcessUtils.getProcess((int)runningProcess.getPID()));
    }

    private synchronized void assemble(ProcessInfo processInfo) {
        this.name = processInfo.getName();
        this.command = processInfo.getCommand();
        this.user = processInfo.getUser();

        try {
            this.cpuUsage = Double.parseDouble(processInfo.getCpuUsage().replace(",", "."));
        } catch (NullPointerException e) {

        }
        this.physicalMemory = Long.parseLong(processInfo.getPhysicalMemory());
        this.virtualMemory = Long.parseLong(processInfo.getVirtualMemory());

        this.pid = Integer.parseInt(processInfo.getPid());
        this.priority = Integer.parseInt(processInfo.getPriority());

        this.time = processInfo.getTime();
        this.startTime = processInfo.getStartTime();
    }

    public synchronized String getName() {
        return name;
    }

    public synchronized String getCommand() {
        return command;
    }

    public synchronized String getUser() {
        return user;
    }

    public synchronized double getCpuUsage() {
        return cpuUsage;
    }

    public synchronized long getPhysicalMemory() {
        return physicalMemory;
    }

    public synchronized long getVirtualMemory() {
        return virtualMemory;
    }

    public synchronized int getPID() {
        return pid;
    }

    public synchronized int getPriority() {
        return priority;
    }

    public synchronized String getTime() {
        return time;
    }

    public synchronized String getStartTime() {
        return startTime;
    }

    public synchronized JProcessesResponse kill() {
        return JProcesses.killProcess(pid);
    }

    public synchronized JProcessesResponse killGracefully() {
        return JProcesses.killProcessGracefully(pid);
    }

    public synchronized JProcessesResponse changePriority(int priority) {
        return JProcesses.changePriority(pid, priority);
    }

    @Override
    public String toString() {
        return StringFormatting.pad(getPID() + "", 5, StringFormatting.Padding.LEFT, false) +
                " " + StringFormatting.pad(getUser(), 10, StringFormatting.Padding.RIGHT, true) +
                " " + StringFormatting.pad(getVirtualMemory() + "", 19, StringFormatting.Padding.LEFT, false) +
                " " + StringFormatting.pad(getPhysicalMemory() + "", 19, StringFormatting.Padding.RIGHT, false) +
                " " + StringFormatting.pad(NumberUtils.round(getCpuUsage(), 1) + "", 5, StringFormatting.Padding.LEFT, false) +
                " " + StringFormatting.pad(getTime(), 10, StringFormatting.Padding.LEFT, false) +
                " " + StringFormatting.pad(getStartTime(), 10, StringFormatting.Padding.RIGHT, false) +
                " " + getCommand();
    }

    public ConsoleMessage toColorString() {
        String DEFAULT = "<DEFAULT:COLOR>";
        String[] COLORS = new String[]{"<GREEN:BRIGHT>", "<YELLOW:BRIGHT>", "<RED:BRIGHT>"};

        StringBuilder st = new StringBuilder();
        st.append(DEFAULT).append(StringFormatting.pad(getPID() + "", 5, StringFormatting.Padding.LEFT, false)).append(" ");
        st.append(DEFAULT).append(StringFormatting.pad(getUser(), 10, StringFormatting.Padding.RIGHT, true)).append(" ");
        st.append(colorMapper(getVirtualMemory(), COLORS, new long[]{100*1024*1024L, 1024*1024*1024L, 10*1024*1024L*1024})).append(StringFormatting.pad(getVirtualMemory() + "", 19, StringFormatting.Padding.LEFT, false)).append(" ");
        st.append(colorMapper(getPhysicalMemory(), COLORS, new long[]{100*1024L, 1024*1024L, 10*1024*1024L})).append(StringFormatting.pad(getPhysicalMemory() + "", 19, StringFormatting.Padding.RIGHT, false)).append(" ");
        st.append(colorMapper(getCpuUsage(), COLORS, new double[]{50.0, 100.0, 150.0})).append(StringFormatting.pad(NumberUtils.round(getCpuUsage(), 1) + "", 5, StringFormatting.Padding.LEFT, false)).append(" ");
        st.append(DEFAULT).append(StringFormatting.pad(getTime(), 10, StringFormatting.Padding.LEFT, false)).append(" ");
        st.append(DEFAULT).append(StringFormatting.pad(getStartTime(), 10, StringFormatting.Padding.RIGHT, false)).append(" ");
        st.append(DEFAULT).append(getCommand());
        return ConsoleMessageBuilder.build(st.toString());
    }

    private String colorMapper(long l, String[] colors, long[] mapper) {
        if (colors.length != mapper.length) {
            return "<DEFAULT>";
        }
        int i = 0;
        while (i < colors.length) {
            if (mapper[i] > l) {
                break;
            }
            i++;
        }
        if (i >= colors.length) {
            return colors[i - 1];
        }
        return colors[i];
    }

    private String colorMapper(double l, String[] colors, double[] mapper) {
        if (colors.length != mapper.length) {
            return "<DEFAULT>";
        }
        int i = 0;
        while (i < colors.length) {
            if (mapper[i] > l) {
                break;
            }
            i++;
        }
        if (i >= colors.length) {
            return colors[i - 1];
        }
        return colors[i];
    }

    public String toCompactString() {
        return StringFormatting.pad(getPID() + "", 5, StringFormatting.Padding.LEFT, false) +
                " " + StringFormatting.pad(getUser(), 10, StringFormatting.Padding.RIGHT, true) +
                " " + StringFormatting.pad(FileUtils.getSize(getVirtualMemory(), true, true, 1), 7, StringFormatting.Padding.LEFT, false) +
                " " + StringFormatting.pad(FileUtils.getSize(getPhysicalMemory(), true, true, 1), 7, StringFormatting.Padding.RIGHT, false) +
                " " + StringFormatting.pad(NumberUtils.round(getCpuUsage(), 1) + "", 5, StringFormatting.Padding.LEFT, false) +
                " " + StringFormatting.pad(getTime(), 10, StringFormatting.Padding.LEFT, false) +
                " " + StringFormatting.pad(getStartTime(), 10, StringFormatting.Padding.RIGHT, false) +
                " " + getCommand();
    }

    public ConsoleMessage toCompactColorString() {
        String DEFAULT = "<DEFAULT:COLOR>";
        String[] COLORS = new String[]{"<GREEN:BRIGHT>", "<YELLOW:BRIGHT>", "<RED:BRIGHT>"};

        StringBuilder st = new StringBuilder();
        st.append(DEFAULT).append(StringFormatting.pad(getPID() + "", 5, StringFormatting.Padding.LEFT, false)).append(" ");
        st.append(DEFAULT).append(StringFormatting.pad(getUser(), 10, StringFormatting.Padding.RIGHT, true)).append(" ");
        st.append(colorMapper(getVirtualMemory(), COLORS, new long[]{100*1024*1024L, 1024*1024*1024L, 10*1024*1024L*1024})).append(StringFormatting.pad(FileUtils.getSize(getVirtualMemory(), true, true, 1), 7, StringFormatting.Padding.LEFT, false)).append(" ");
        st.append(colorMapper(getPhysicalMemory(), COLORS, new long[]{100*1024L, 1024*1024L, 10*1024*1024L})).append(StringFormatting.pad(FileUtils.getSize(getPhysicalMemory(), true, true, 1), 7, StringFormatting.Padding.RIGHT, false)).append(" ");
        st.append(colorMapper(getCpuUsage(), COLORS, new double[]{50.0, 100.0, 150.0})).append(StringFormatting.pad(NumberUtils.round(getCpuUsage(), 1) + "", 5, StringFormatting.Padding.LEFT, false)).append(" ");
        st.append(DEFAULT).append(StringFormatting.pad(getTime(), 10, StringFormatting.Padding.LEFT, false)).append(" ");
        st.append(DEFAULT).append(StringFormatting.pad(getStartTime(), 10, StringFormatting.Padding.RIGHT, false)).append(" ");
        st.append(DEFAULT).append(getCommand());
        return ConsoleMessageBuilder.build(st.toString());
    }

    public static String legendString() {
        StringBuilder st = new StringBuilder();
        st.append(StringFormatting.pad("PID", 5, StringFormatting.Padding.LEFT, false)).append(" ");
        st.append(StringFormatting.pad("USER", 10, StringFormatting.Padding.RIGHT, false)).append(" ");
        st.append(StringFormatting.pad("V-mem", 7, StringFormatting.Padding.LEFT, false)).append(" ");
        st.append(StringFormatting.pad("P-mem", 7, StringFormatting.Padding.RIGHT, false)).append(" ");
        st.append(StringFormatting.pad("CPU", 5, StringFormatting.Padding.LEFT, false)).append(" ");
        st.append(StringFormatting.pad("TIME", 10, StringFormatting.Padding.LEFT, false)).append(" ");
        st.append(StringFormatting.pad("S-time", 10, StringFormatting.Padding.RIGHT, false)).append(" ");
        st.append("COMMAND").append(" ");
        return st.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof YProcess)) return false;
        YProcess process = (YProcess) o;
        return pid == process.pid;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pid);
    }

}