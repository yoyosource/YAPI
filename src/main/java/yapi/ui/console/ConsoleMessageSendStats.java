// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.ui.console;

public class ConsoleMessageSendStats {

    public final int previousTasks;
    public final int optimizedTasks;
    public final double deflationRate;

    public final long renderTime;
    public final long printTime;
    public final double tasksPerSecond;

    ConsoleMessageSendStats(int previousTasks, int optimizedTasks, long renderTime, long printTime) {
        this.previousTasks = previousTasks;
        this.optimizedTasks = optimizedTasks;
        deflationRate = 1.0 - (double)optimizedTasks / previousTasks;
        this.renderTime = renderTime;
        this.printTime = printTime;
        tasksPerSecond = (double) previousTasks / renderTime * 1000.0;
    }

    @Override
    public String toString() {
        return "ConsoleMessageSendStats{" +
                "previousTasks=" + previousTasks +
                ", optimizedTasks=" + optimizedTasks +
                ", deflationRate=" + deflationRate +
                ", renderTime=" + renderTime +
                ", printTime=" + printTime +
                ", tasksPerSecond=" + tasksPerSecond +
                '}';
    }

}