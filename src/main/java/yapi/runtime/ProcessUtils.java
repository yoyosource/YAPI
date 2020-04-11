// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.runtime;

import java.util.List;
import java.util.stream.Collectors;

public class ProcessUtils {

    private ProcessUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static long getPID() {
        return ProcessHandle.current().pid();
    }

    public RunningProcess getCurrentProcess() {
        return new RunningProcess(ProcessHandle.current());
    }

    public List<RunningProcess> getRunningProesses() {
        return ProcessHandle.allProcesses().map(pH -> new RunningProcess(pH)).collect(Collectors.toList());
    }

}