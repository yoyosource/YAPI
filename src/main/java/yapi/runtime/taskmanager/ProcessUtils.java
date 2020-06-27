// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.runtime.taskmanager;

import org.jutils.jprocesses.JProcesses;
import org.jutils.jprocesses.model.JProcessesResponse;
import org.jutils.jprocesses.model.ProcessInfo;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class ProcessUtils {

    private ProcessUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static List<ProcessInfo> getProcesses() {
        return JProcesses.getProcessList();
    }

    public static List<YProcess> getYProcesses() {
        List<YProcess> yProcessList = new LinkedList<>();
        List<ProcessInfo> processInfos = getProcesses();
        for (ProcessInfo processInfo : processInfos) {
            yProcessList.add(YProcess.getInstance(processInfo));
        }
        yProcessList.sort(Comparator.comparingInt(YProcess::getPID));
        return yProcessList;
    }

    public static List<YProcess> getYProcesses(Comparator<YProcess> comparator) {
        List<YProcess> yProcesses = getYProcesses();
        yProcesses.sort(comparator);
        return yProcesses;
    }

    public static List<ProcessInfo> getProcesses(String name) {
        return JProcesses.getProcessList(name);
    }

    public static List<YProcess> getYProcesses(String name) {
        List<YProcess> yProcessList = new LinkedList<>();
        List<ProcessInfo> processInfos = getProcesses(name);
        for (ProcessInfo processInfo : processInfos) {
            yProcessList.add(YProcess.getInstance(processInfo));
        }
        return yProcessList;
    }

    public static ProcessInfo getProcess(int pid) {
        return JProcesses.getProcess(pid);
    }

    public static JProcessesResponse killProcess(int pid) {
        return JProcesses.killProcess(pid);
    }

    public static JProcessesResponse[] killProcesses(int... pids) {
        JProcessesResponse[] responses = new JProcessesResponse[pids.length];
        for (int i = 0; i < pids.length; i++) {
            responses[i] = JProcesses.killProcess(pids[i]);
        }
        return responses;
    }

    public static JProcessesResponse killProcessGracefully(int pid) {
        return JProcesses.killProcessGracefully(pid);
    }

    public static JProcessesResponse[] killProcessGracefully(int... pids) {
        JProcessesResponse[] responses = new JProcessesResponse[pids.length];
        for (int i = 0; i < pids.length; i++) {
            responses[i] = JProcesses.killProcessGracefully(pids[i]);
        }
        return responses;
    }

    public static JProcessesResponse changePriority(int pid, int priority) {
        return JProcesses.changePriority(pid, priority);
    }

    public static JProcessesResponse[] changePriority(int[] pids, int priority) {
        JProcessesResponse[] responses = new JProcessesResponse[pids.length];
        for (int i = 0; i < pids.length; i++) {
            responses[i] = changePriority(pids[i], priority);
        }
        return responses;
    }

}