// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.runtime;

import yapi.os.OSCheck;
import yapi.os.OSType;

import java.awt.*;
import java.io.IOException;

public class RuntimeUtils {

    private RuntimeUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static void addSutdownHook(Hook hook) {
        Runtime.getRuntime().addShutdownHook(HookManager.getHookThread(hook));
    }

    public static void removeShutdownHook(Hook hook) {
        Runtime.getRuntime().removeShutdownHook(HookManager.getHookThread(hook));
    }

    public static int availableProcessors() {
        return Runtime.getRuntime().availableProcessors();
    }

    public static void runGarbageCollector() {
        Runtime.getRuntime().gc();
    }

    public static Process exec(String s) throws IOException {
        return executeTerminalCommand(s);
    }

    public static Process executeTerminalCommand(String s) throws IOException {
        return Runtime.getRuntime().exec(s);
    }

    public static Process exec(String... strings) throws IOException {
        return executeTerminalCommand(strings);
    }

    public static Process executeTerminalCommand(String... strings) throws IOException {
        return Runtime.getRuntime().exec(strings);
    }

    public static void halt(int status) {
        Runtime.getRuntime().halt(status);
    }

    public static void exit(int status) {
        Runtime.getRuntime().exit(status);
    }

    public static Runtime.Version getVersion() {
        return Runtime.version();
    }

    public static OSType getOSType() {
        return OSCheck.getType();
    }

    public static long getPID() {
        return CurrentProcessUtils.getPID();
    }

    public static boolean isHeadless() {
        return GraphicsEnvironment.isHeadless();
    }

}