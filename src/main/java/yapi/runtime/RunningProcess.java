// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.runtime;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class RunningProcess {

    private ProcessHandle processHandle;

    RunningProcess(ProcessHandle processHandle) {
        this.processHandle = processHandle;
    }

    public long getPID() {
        return processHandle.pid();
    }

    public RunningProcess getParent() {
        if (processHandle.parent().isPresent()) {
            return new RunningProcess(processHandle.parent().get());
        } else {
            return null;
        }
    }

    public boolean destroy() {
        return processHandle.destroy();
    }

    public boolean destroyForcibly() {
        return processHandle.destroyForcibly();
    }

    public ProcessHandle.Info info() {
        return processHandle.info();
    }

    public boolean isAlive() {
        return processHandle.isAlive();
    }

    public boolean supportsNormalTermination() {
        return processHandle.supportsNormalTermination();
    }

    public List<RunningProcess> children() {
        return processHandle.children().map(pH -> new RunningProcess(pH)).collect(Collectors.toList());
    }

    public int childrentCount() {
        return children().size();
    }

    public List<RunningProcess> descendants() {
        return processHandle.descendants().map(pH -> new RunningProcess(pH)).collect(Collectors.toList());
    }

    public int descendantsCount() {
        return descendants().size();
    }

    public CompletableFuture<ProcessHandle> onExit() {
        return processHandle.onExit();
    }

    public ProcessHandle getProcessHandle() {
        return processHandle;
    }

    public boolean isCurrentProcess() {
        return getPID() == CurrentProcessUtils.getPID();
    }

}