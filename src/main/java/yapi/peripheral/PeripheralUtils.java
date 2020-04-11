// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.peripheral;

import yapi.runtime.RuntimeUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Locale;

public class PeripheralUtils {

    private PeripheralUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static GraphicsEnvironment getGraphicsEnvironment() {
        return getLocalGraphicsEnvironment();
    }

    public static GraphicsEnvironment getLocalGraphicsEnvironment() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment();
    }

    public static GraphicsDevice[] getScreenDevices() {
        return getLocalGraphicsEnvironment().getScreenDevices();
    }

    public static GraphicsDevice getDefaultScreenDevice() {
        return getLocalGraphicsEnvironment().getDefaultScreenDevice();
    }

    public static Image takeScreenShot(Window window) {
        return window.createImage(window.getWidth(), window.getHeight());
    }

    public static Image takeScreenShot(GraphicsDevice graphicsDevice) {
        return takeScreenShot(graphicsDevice.getFullScreenWindow());
    }

    public static Image takeScreenShot() {
        return takeScreenShot(getDefaultScreenDevice());
    }

    public static Image[] takeScreenShots() {
        GraphicsDevice[] graphicsDevices = getScreenDevices();
        Image[] images = new Image[graphicsDevices.length];
        for (int i = 0; i < graphicsDevices.length; i++) {
            images[i] = takeScreenShot(graphicsDevices[i]);
        }
        return images;
    }

    /*public static void takeScreenShotRobot(GraphicsDevice graphicsDevice) {
        Robot robot = new Robot();
        robot.createScreenCapture()
    }*/

    public static boolean isHeadless() {
        return RuntimeUtils.isHeadless();
    }

    public static boolean isHeadlessInstance() {
        return getLocalGraphicsEnvironment().isHeadlessInstance();
    }

    public static Font[] getAllFonts() {
        return getLocalGraphicsEnvironment().getAllFonts();
    }

    public static Graphics2D createGraphics(BufferedImage img) {
        if (img == null) {
            return null;
        }
        return getLocalGraphicsEnvironment().createGraphics(img);
    }

    public static String[] getAvailableFontFamilyNames() {
        return getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
    }

    public static String[] getAvailableFontFamilyNames(Locale l) {
        return getLocalGraphicsEnvironment().getAvailableFontFamilyNames(l);
    }

    public static void registerFont(Font font) {
        if (font == null) {
            return;
        }
        getLocalGraphicsEnvironment().registerFont(font);
    }

    public static void preferLocaleFonts() {
        getLocalGraphicsEnvironment().preferLocaleFonts();
    }

    public static void preferProportionalFonts() {
        getLocalGraphicsEnvironment().preferProportionalFonts();
    }

    public static Point getCenterPoint() {
        return getLocalGraphicsEnvironment().getCenterPoint();
    }

    public static Rectangle getMaximumWindowBounds() {
        return getLocalGraphicsEnvironment().getMaximumWindowBounds();
    }

}