// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.os;

import javax.management.*;
import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;

public class OSUtils {

    private OSUtils() {
        throw new IllegalStateException("Utility class");
    }

    private static OperatingSystemMXBean getOperatingSystemMXBean() {
        return (OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();
    }

    public static String getArch() {
        return getOperatingSystemMXBean().getArch();
    }

    public static double getSystemLoadAverage() {
        return getOperatingSystemMXBean().getSystemLoadAverage();
    }

    public static long getCommittedVirtualMemorySize() {
        return getOperatingSystemMXBean().getCommittedVirtualMemorySize();
    }

    public static long getFreePhysicalMemorySize() {
        return getOperatingSystemMXBean().getFreePhysicalMemorySize();
    }

    public static long getTotalPhysicalMemorySize() {
        return getOperatingSystemMXBean().getTotalPhysicalMemorySize();
    }

    public static long getFreeSwapSpaceSize() {
        return getOperatingSystemMXBean().getFreeSwapSpaceSize();
    }

    public static long getTotalSwapSpaceSize() {
        return getOperatingSystemMXBean().getTotalSwapSpaceSize();
    }

    public static double getProcessCPULoad() {
        return getOperatingSystemMXBean().getProcessCpuLoad();
    }

    public static double getProcessCPULoad(boolean alternative) {
        if (!alternative) {
            return getProcessCPULoad();
        }
        try {
            MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
            ObjectName name = ObjectName.getInstance("java.lang:type=OperatingSystem");
            AttributeList list = mBeanServer.getAttributes(name, new String[]{"ProcessCpuLoad"});

            if (list.isEmpty()) {
                return Double.NaN;
            }

            Attribute attribute = (Attribute) list.get(0);
            Double load = (Double) attribute.getValue();

            if (load == -1) {
                return Double.NaN;
            }
            return ((int) (load * 1000) / 10.0);
        } catch (MalformedObjectNameException | ReflectionException | InstanceNotFoundException e) {
            return Double.NaN;
        }
    }

    public static double getSystemCpuLoad() {
        return getOperatingSystemMXBean().getSystemCpuLoad();
    }

    public static long getProcessCpuTime() {
        return getOperatingSystemMXBean().getProcessCpuTime();
    }

    public static long totalMemory() {
        return Runtime.getRuntime().totalMemory();
    }

    public static long freeMemory() {
        return Runtime.getRuntime().freeMemory();
    }

    public static long maxMemory() {
        return Runtime.getRuntime().maxMemory();
    }

}