package yapi.runtime.taskmanager;

import org.jutils.jprocesses.JProcesses;
import org.jutils.jprocesses.model.JProcessesResponse;
import org.jutils.jprocesses.model.ProcessInfo;
import yapi.runtime.RunningProcess;
import yapi.runtime.ThreadUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class YProcess {

    private static Thread updater;
    private static List<YProcess> allProcesses = new LinkedList<>();

    private static void createUpdater() {
        if (updater != null) {
            return;
        }

        updater = new Thread(() -> {
            while (true) {
                List<ProcessInfo> processInfos = ProcessUtils.getProcesses();
                List<Integer> pids = createPIDList(processInfos);

                for (int i = allProcesses.size() - 1; i >= 0; i--) {
                    YProcess process = allProcesses.get(i);
                    if (!pids.contains(process.getPid())) {
                        allProcesses.remove(i);
                        continue;
                    }
                    process.assemble(processInfos.get(pids.indexOf(process.getPid())));
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
    private double physicalMemory;
    private double virtualMemory;

    private int pid;
    private int priority;

    private String time;
    private String startTime;

    public static YProcess getInstance(ProcessInfo processInfo) {
        for (YProcess process : allProcesses) {
            if (process.getPid() == Integer.parseInt(processInfo.getPid())) return process;
        }
        return new YProcess(processInfo);
    }

    public static YProcess getInstance(RunningProcess runningProcess) {
        for (YProcess process : allProcesses) {
            if (process.getPid() == (int)runningProcess.getPID()) return process;
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

        this.cpuUsage = Double.parseDouble(processInfo.getCpuUsage());
        this.physicalMemory = Double.parseDouble(processInfo.getPhysicalMemory());
        this.virtualMemory = Double.parseDouble(processInfo.getVirtualMemory());

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

    public synchronized double getPhysicalMemory() {
        return physicalMemory;
    }

    public synchronized double getVirtualMemory() {
        return virtualMemory;
    }

    public synchronized int getPid() {
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
