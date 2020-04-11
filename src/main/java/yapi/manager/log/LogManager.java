// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.manager.log;

import yapi.file.FileUtils;
import yapi.math.base.BaseConversion;
import yapi.math.NumberRandom;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class LogManager {

    public static void main(String[] args) {
        BigInteger bigInteger = BigInteger.valueOf(94).pow(8);
        System.out.println(bigInteger);

        //                         1 -  0
        //                       127 -  8
        //                    65.535 - 16
        //             2.147.483.647 - 32
        // 9.223.372.036.854.775.807 - 64

        if (false) {
            long i = 0;
            while (true) {
                if (i % 1048576 == 0) {
                    System.out.println(i);
                }
                Logging logging = new Logging();
                i++;
            }
        }
    }

    private static Map<String, Logging> loggingMap = new HashMap<>();
    private static NumberRandom numberRandom = new NumberRandom();
    private static File logPackage = null;
    private static PrintStream printStream = null;

    static {
        setAllowLogDefault();
    }

    private static Logging logging = new Logging("LogManager", 0);

    private LogManager() {
        throw new IllegalStateException("Utility class");
    }

    public static void setAllowLog(File logPackage) {
        if (!logPackage.exists()) {
            logPackage.mkdirs();
        }
        if (!logPackage.isDirectory()) {
            return;
        }
        LogManager.logPackage = logPackage;
        File logFile = new File(logPackage + "/" + fileName() + ".log");
        try {
            logFile.createNewFile();
            printStream = new PrintStream(logFile);
            //printStream.println("[R] GLog > '" + logPackage + "'");
        } catch (IOException e) {

        }
    }

    public static void setAllowLogDefault() {
        setAllowLog(defaultLogPackage());
    }

    private static File defaultLogPackage() {
        return new File(FileUtils.getUserHome() + "/YAPI/log");
    }

    static LogUUID createUUID() {
        int tries = 0;
        while (true) {
            if (tries > 1_000_000_000) {
                return new LogUUID("");
            }
            String s = BaseConversion.toBase64(numberRandom.getInt());
            s = "0".repeat(6 - s.length()) + s;
            if (!loggingMap.containsKey(s)) {
                if (logging != null) {
                    logging.add("[C] UUID > " + s);
                }
                return new LogUUID(s);
            }
            tries++;
        }
    }

    static LogUUID createUUID(int uuid) {
        if (!loggingMap.containsKey(BaseConversion.toBase64(uuid))) {
            String s = BaseConversion.toBase64(uuid);
            s = "0".repeat(6 - s.length()) + s;
            if (logging != null) {
                logging.add("[A] UUID > " + s);
            }
            return new LogUUID(s );
        }
        return createUUID();
    }

    static void registerLog(LogUUID uuid, Logging logging) {
        logging.add("[R] Log  > " + uuid);
        loggingMap.put(uuid.toString(), logging);
    }

    static synchronized void add(LogEntry... entries) {
        if (logPackage == null) {
            return;
        }
        for (LogEntry entry : entries) {
            printStream.println(entry.toString());
        }
        printStream.flush();
    }

    private static String fileName() {
        return DateTimeFormatter.ofPattern("dd-MM-uuuu HH-mm-ss").format(LocalDateTime.now());
    }

}