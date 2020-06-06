package yapi.runtime.taskmanager;

import org.jutils.jprocesses.JProcesses;
import org.jutils.jprocesses.model.ProcessInfo;

public class CPUUtils {

    private CPUUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static double getCPUUsage() {
        double cpuUsage = 0;
        for (ProcessInfo processInfo : JProcesses.getProcessList()) {
            cpuUsage += getCPUUsage(processInfo);
        }
        return cpuUsage;
    }

    public static double getCPUUsage(ProcessInfo info) {
        return Double.parseDouble(info.getCpuUsage());
    }

    public static double getPhysicalMemory() {
        double physicalMemory = 0;
        for (ProcessInfo processInfo : JProcesses.getProcessList()) {
            physicalMemory += getPhysicalMemory(processInfo);
        }
        return physicalMemory;
    }

    public static double getPhysicalMemory(ProcessInfo info) {
        return Double.parseDouble(info.getPhysicalMemory());
    }

    public static double getVirtualMemory() {
        double virtualMemory = 0;
        for (ProcessInfo processInfo : JProcesses.getProcessList()) {
            virtualMemory += getVirtualMemory(processInfo);
        }
        return virtualMemory;
    }

    public static double getVirtualMemory(ProcessInfo info) {
        return Double.parseDouble(info.getVirtualMemory());
    }

}
