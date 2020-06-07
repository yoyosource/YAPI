// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.ui;

import org.jutils.jprocesses.JProcesses;
import org.jutils.jprocesses.model.ProcessInfo;
import yapi.file.FileUtils;
import yapi.math.Fraction;
import yapi.math.NumberUtils;
import yapi.runtime.ThreadUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TestTest {

    public static void main(String[] args) {
        test();
        //Clip clip = AudioSystem.getClip();
        //clip.loop();

        //productSystem(3);
        //System.out.println();
        //System.out.println(count);

        if (false) {
            int count = 7;
            double d1 = 0.9;
            double d2 = 0.1;

            StringBuilder st = new StringBuilder();
            for (int i = 0; i <= count; i++) {
                st.append(NumberUtils.over(count, i)).append("*").append(d1).append("^{").append(count - i).append("}*").append(d2).append("^{").append(i).append("}+");
            }
            System.out.println(st.toString());
        }

        if (true) {
            int count = 5;
            Fraction percent = Fraction.decode("65%");

            for (int i = 0; i <= count; i++) {
                System.out.println(i + ": " + (new Fraction(NumberUtils.over(count, i)).multiply(percent.power(i).multiply(Fraction.ONE.subtract(percent).power(count - i)))).encodePercent());
            }
        }
    }

    private static void test() {
        System.out.println(FileUtils.getSize(FileUtils.getDiskSpace(), true, true, 1));
        System.out.println(FileUtils.getSize(FileUtils.getFreeSpace(), true, true, 1));
        System.out.println(FileUtils.getSize(FileUtils.getUsableSpace(), true, true, 1));
        System.out.println();

        List<ProcessInfo> processesList = JProcesses.getProcessList();

        /*double cpuUsage = 0;
        for (final ProcessInfo processInfo : processesList) {
            //send(processInfo);
            cpuUsage += getCPUUsage(processInfo);

            System.out.println("Process PID: " + processInfo.getPid());
            System.out.println("Process Name: " + processInfo.getName());
            System.out.println("Process Time: " + processInfo.getTime());
            System.out.println("User: " + processInfo.getUser());
            System.out.println("Virtual Memory: " + processInfo.getVirtualMemory());
            System.out.println("Physical Memory: " + processInfo.getPhysicalMemory());
            System.out.println("CPU usage: " + processInfo.getCpuUsage());
            System.out.println("Start Time: " + processInfo.getStartTime());
            System.out.println("Priority: " + processInfo.getPriority());
            System.out.println("Full command: " + processInfo.getCommand());
            System.out.println("------------------");

        }

        System.out.println(cpuUsage);
        */

        for (;;) {
            double usage = getCPUUsage();
            System.out.println(usage + "/" + Runtime.getRuntime().availableProcessors() + " ~ " + (usage / Runtime.getRuntime().availableProcessors()));
            ThreadUtils.sleep(10);
        }
    }

    private static void send(ProcessInfo info) {
        System.out.println(pad(5, info.getPid()) + " " + pad(12, info.getName()) + " " + pad(5, info.getCpuUsage()) + " ");
    }

    private static double getCPUUsage() {
        double cpuUsage = 0;
        for (ProcessInfo processInfo : JProcesses.getProcessList()) {
            cpuUsage += getCPUUsage(processInfo);
        }
        return cpuUsage;
    }

    private static double getCPUUsage(ProcessInfo info) {
        return Double.parseDouble(info.getCpuUsage());
    }

    private static String pad(int pad, Object object) {
        if (object.toString().length() > pad) {
            return object.toString().substring(0, pad);
        }
        return " ".repeat(pad - object.toString().length()) + object;
    }

    private static int count = 0;

    private static void productSystem(int diceNumber) {
        int[] dices = new int[diceNumber];
        Arrays.fill(dices, 1);

        Map<Integer, Integer> integerIntegerMap = new HashMap<>();
        double d = Math.pow(6, diceNumber);
        for (long l = 0; l < d; l++) {
            int index = 0;
            while (index != -1) {
                if (index >= dices.length) {
                    break;
                }
                dices[index]++;
                if (dices[index] > 6) {
                    dices[index] = 1;
                    index++;
                } else {
                    index = -1;
                }
            }

            appendToMap(integerIntegerMap, dices);
        }

        List<Integer> keys = integerIntegerMap.keySet().stream().collect(Collectors.toList());
        keys.sort(Integer::compareTo);
        for (int k : keys) {
            System.out.print(k + "=" + integerIntegerMap.get(k) + ", ");
        }
    }

    private static void appendToMap(Map<Integer, Integer> integerIntegerMap, int[] dices) {
        int l = 1;
        //System.out.print(Arrays.toString(dices) + " " + count + " ");
        count = 0;
        for (int i : dices) {
            if (i == 3 || i == 4) {
                count++;
            }
            l *= i;
        }
        l = count;
        //System.out.println(count);
        if (integerIntegerMap.containsKey(l)) {
            integerIntegerMap.replace(l, integerIntegerMap.get(l) + 1);
        } else {
            integerIntegerMap.put(l, 1);
        }
    }

}