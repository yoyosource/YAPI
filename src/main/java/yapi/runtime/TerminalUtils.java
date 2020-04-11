// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.runtime;

import org.fusesource.jansi.internal.WindowsSupport;
import sun.misc.Signal;
import sun.misc.SignalHandler;
import yapi.os.OSCheck;
import yapi.os.OSType;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class TerminalUtils {

    private TerminalUtils() {
        throw new IllegalStateException("Utility class");
    }

    private static Map<Long, Hook> resizeHooks = new HashMap<>();
    private static Dimension lastDimensions = getDimensions();

    private static boolean handler = false;
    private static boolean running = true;
    private static long lastResize = 0;
    private static Thread resizeThread = new Thread(() -> {
        while (running) {
            while (lastResize == 0) {
                ThreadUtils.sleep(1);
            }
            while (System.currentTimeMillis() - lastResize < 100) {
                ThreadUtils.sleep(1);
            }
            lastResize = 0;
            callResizeHooks();
        }
    });

    private static void startResizeHandler() {
        OSType osType = getOSType();

        switch (osType) {
            case MAC_OS:
            case LINUX:
                try {
                    createResizeEventHandle();
                } catch (Exception e) {
                    createResizeEventThread();
                }
                break;
            case WINDOWS:
                createResizeEventThread();
                break;
            case OTHER:
                throw new UnsupportedOperationException("OSType unknown. Cannot create Resize Hook");
        }

        resizeThread.setName("Resize Event Handler");
    }

    private static void callResizeHooks() {
        for (Map.Entry<Long, Hook> e : resizeHooks.entrySet()) {
            HookManager.getHookThread(e.getValue()).start();
        }
    }

    private static void createResizeEventThread() {
        Thread eventThread = new Thread(() -> {
            while (running) {
                ThreadUtils.sleep(10);

                Dimension dim = getDimensions();
                if (dim.width != lastDimensions.width || dim.height != lastDimensions.height) {
                    resizing();
                }
                lastDimensions = dim;
            }
        });

        eventThread.setName("Resize Event");
        eventThread.setDaemon(true);
        eventThread.start();
    }

    private static void createResizeEventHandle() throws Exception {
        SignalHandler signalHandler = (Signal sig) -> {
            if (!sig.getName().equals("WINCH")) {
                return;
            }
            resizing();
        };
        Signal.handle(new Signal("WINCH"), signalHandler);
    }

    private static void resizing() {
        lastResize = System.currentTimeMillis();
    }

    public static synchronized void addResizeHook(Hook hook) {
        if (!handler) {
            startResizeHandler();

            lastResize = System.currentTimeMillis();
            resizeThread.setDaemon(true);
            resizeThread.start();

            handler = true;
        }
        resizeHooks.put(hook.getId(), hook);
    }

    public static synchronized void removeResizeHook(Hook hook) {
        resizeHooks.remove(hook.getId());
    }

    public static void stopResizeEventHanlder() {
        running = false;
    }

    public static OSType getOSType() {
        return OSCheck.getType();
    }

    public static int getWidth() {
        OSType type = getOSType();

        switch (type) {
            case MAC_OS:
                return getSizeMacOS().width;
            case LINUX:
                return getSizeLinux().width;
            case WINDOWS:
                return getSizeWindows().width;
            default:
                break;
        }

        return -1;
    }

    public static int getWidthOrDefault() {
        int i = getWidth();
        if (i == -1) {
            return 80;
        }
        return i;
    }

    public static int getHeight() {
        OSType type = getOSType();

        switch (type) {
            case MAC_OS:
                return getSizeMacOS().height;
            case LINUX:
                return getSizeLinux().height;
            case WINDOWS:
                return getSizeWindows().height;
            default:
                break;
        }

        return -1;
    }

    public static int getHeightOrDefault() {
        int i = getHeight();
        if (i == -1) {
            return 24;
        }
        return i;
    }

    public static Dimension getDimensions() {
        OSType type = getOSType();

        switch (type) {
            case MAC_OS:
                return getSizeMacOS();
            case LINUX:
                return getSizeLinux();
            case WINDOWS:
                return getSizeWindows();
            default:
                break;
        }

        return new Dimension(-1, -1);
    }

    public static Dimension getDimensionsOrDefault() {
        Dimension dimension = getDimensions();
        if (dimension.width == -1 || dimension.height == -1) {
            return new Dimension(80, 24);
        }
        return dimension;
    }

    public static Dimension getDefaultSizeMacOS() {
        return new Dimension(80, 24);
    }

    private static Dimension getSizeMacOS() {
        try {
            int width = getInt(RuntimeUtils.exec("bash", "-c", "tput cols 2> /dev/tty").getInputStream());
            int height = getInt(RuntimeUtils.exec("bash", "-c", "tput lines 2> /dev/tty").getInputStream());
            if (width == -1 || height == -1) {
                throw new IOException();
            }
            return new Dimension(width, height);
        } catch (IOException e) {

        }
        return new Dimension(80, 24);
    }

    public static Dimension getDefaultSizeLinux() {
        return new Dimension(80, 32);
    }

    private static Dimension getSizeLinux() {
        // TODO: Untested
        try {
            String[] s = new BufferedReader(new InputStreamReader(RuntimeUtils.exec("stty size").getInputStream())).readLine().split(" ");
            int width = getInt(s[1]);
            int height = getInt(s[0]);
            if (width == -1 || height == -1) {
                throw new IOException();
            }
            return new Dimension(width, height);
        } catch (IOException e) {

        }
        return new Dimension(80, 32);
    }

    public static Dimension getDefaultSizeWindows() {
        return new Dimension(120, 30);
    }

    private static Dimension getSizeWindows() {
        try {
            int width = WindowsSupport.getWindowsTerminalWidth();
            int height = WindowsSupport.getWindowsTerminalHeight();
            if (width == -1 || height == -1) {
                throw new IOException();
            }
            return new Dimension(width, height);
        } catch (Exception e) {

        }
        return new Dimension(120, 30);
    }

    private static int getInt(InputStream inputStream) throws IOException {
        return getInt(new BufferedReader(new InputStreamReader(inputStream)).readLine());
    }

    private static int getInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

}