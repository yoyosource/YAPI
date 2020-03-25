// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.ui.console;

import org.fusesource.jansi.Ansi;
import yapi.color.ColorUtils;

import java.util.ArrayList;
import java.util.List;

enum ConsoleBuildAttribute {

    DEFAULT("default"),
    DEFAULT_COLOR("default:color", new TaskColor(Ansi.Color.DEFAULT)),
    DEFAULT_ATTRIBUTE("default:attribute", new TaskAttribute(Ansi.Attribute.RESET)),
    DEFAULT_ALIGNMENT("default:alignment", new TaskAlignment(ConsoleAlignment.LEFT)),

    ERASE_LINE_ALL("erase:line:all", new TaskEraseLine(Ansi.Erase.ALL)),
    ERASE_LINE_FORWARD("erase:line:forward", new TaskEraseLine(Ansi.Erase.FORWARD)),
    ERASE_LINE_BACKWARD("erase:line:backward", new TaskEraseLine(Ansi.Erase.BACKWARD)),
    ERASE_SCREEN_ALL("erase:screen:all", new TaskEraseScreen(Ansi.Erase.ALL)),
    ERASE_SCREEN_FORWARD("erase:screen:forward", new TaskEraseScreen(Ansi.Erase.FORWARD)),
    ERASE_SCREEN_BACKWARD("erase:screen:backward", new TaskEraseScreen(Ansi.Erase.BACKWARD)),

    COLOR_BLACK("black", new TaskColor(Ansi.Color.BLACK), new TaskColor(Ansi.Color.DEFAULT)),
    COLOR_WHITE("white", new TaskColor(Ansi.Color.WHITE), new TaskColor(Ansi.Color.DEFAULT)),
    COLOR_RED("red", new TaskColor(Ansi.Color.RED), new TaskColor(Ansi.Color.DEFAULT)),
    COLOR_GREEN("green", new TaskColor(Ansi.Color.GREEN), new TaskColor(Ansi.Color.DEFAULT)),
    COLOR_BLUE("blue", new TaskColor(Ansi.Color.BLUE), new TaskColor(Ansi.Color.DEFAULT)),
    COLOR_YELLOW("yellow", new TaskColor(Ansi.Color.YELLOW), new TaskColor(Ansi.Color.DEFAULT)),
    COLOR_MAGENTA("magenta", new TaskColor(Ansi.Color.MAGENTA), new TaskColor(Ansi.Color.DEFAULT)),
    COLOR_CYAN("cyan", new TaskColor(Ansi.Color.CYAN), new TaskColor(Ansi.Color.DEFAULT)),

    COLOR_BLACK_BRIGHT("black:bright", new TaskColor(Ansi.Color.BLACK), new TaskColor(Ansi.Color.DEFAULT)),
    COLOR_WHITE_BRIGHT("white:bright", new TaskColor(Ansi.Color.WHITE), new TaskColor(Ansi.Color.DEFAULT)),
    COLOR_RED_BRIGHT("red:bright", new TaskColor(Ansi.Color.RED), new TaskColor(Ansi.Color.DEFAULT)),
    COLOR_GREEN_BRIGHT("green:bright", new TaskColor(Ansi.Color.GREEN), new TaskColor(Ansi.Color.DEFAULT)),
    COLOR_BLUE_BRIGHT("blue:bright", new TaskColor(Ansi.Color.BLUE), new TaskColor(Ansi.Color.DEFAULT)),
    COLOR_YELLOW_BRIGHT("yellow:bright", new TaskColor(Ansi.Color.YELLOW), new TaskColor(Ansi.Color.DEFAULT)),
    COLOR_MAGENTA_BRIGHT("magenta:bright", new TaskColor(Ansi.Color.MAGENTA), new TaskColor(Ansi.Color.DEFAULT)),
    COLOR_CYAN_BRIGHT("cyan:bright", new TaskColor(Ansi.Color.CYAN), new TaskColor(Ansi.Color.DEFAULT)),

    COLOR_BACK_BG("black:bg", new TaskBGColor(Ansi.Color.BLACK), new TaskBGColor(Ansi.Color.DEFAULT)),
    COLOR_WHITE_BG("white:bg", new TaskBGColor(Ansi.Color.WHITE), new TaskBGColor(Ansi.Color.DEFAULT)),
    COLOR_RED_BG("red:bg", new TaskBGColor(Ansi.Color.RED), new TaskBGColor(Ansi.Color.DEFAULT)),
    COLOR_GREEN_BG("green:bg", new TaskBGColor(Ansi.Color.GREEN), new TaskBGColor(Ansi.Color.DEFAULT)),
    COLOR_BLUE_BG("blue:bg", new TaskBGColor(Ansi.Color.BLUE), new TaskBGColor(Ansi.Color.DEFAULT)),
    COLOR_YELLOW_BG("yellow:bg", new TaskBGColor(Ansi.Color.YELLOW), new TaskBGColor(Ansi.Color.DEFAULT)),
    COLOR_MAGENTA_BG("magenta:bg", new TaskBGColor(Ansi.Color.MAGENTA), new TaskBGColor(Ansi.Color.DEFAULT)),
    COLOR_CYAN_BG("cyan:bg", new TaskBGColor(Ansi.Color.CYAN), new TaskBGColor(Ansi.Color.DEFAULT)),

    COLOR_BACK_BG_BRIGHT("black:bg:bright", new TaskBGColor(Ansi.Color.BLACK), new TaskBGColor(Ansi.Color.DEFAULT)),
    COLOR_WHITE_BG_BRIGHT("white:bg:bright", new TaskBGColor(Ansi.Color.WHITE), new TaskBGColor(Ansi.Color.DEFAULT)),
    COLOR_RED_BG_BRIGHT("red:bg:bright", new TaskBGColor(Ansi.Color.RED), new TaskBGColor(Ansi.Color.DEFAULT)),
    COLOR_GREEN_BG_BRIGHT("green:bg:bright", new TaskBGColor(Ansi.Color.GREEN), new TaskBGColor(Ansi.Color.DEFAULT)),
    COLOR_BLUE_BG_BRIGHT("blue:bg:bright", new TaskBGColor(Ansi.Color.BLUE), new TaskBGColor(Ansi.Color.DEFAULT)),
    COLOR_YELLOW_BG_BRIGHT("yellow:bg:bright", new TaskBGColor(Ansi.Color.YELLOW), new TaskBGColor(Ansi.Color.DEFAULT)),
    COLOR_MAGENTA_BG_BRIGHT("magenta:bg:bright", new TaskBGColor(Ansi.Color.MAGENTA), new TaskBGColor(Ansi.Color.DEFAULT)),
    COLOR_CYAN_BG_BRIGHT("cyan:bg:bright", new TaskBGColor(Ansi.Color.CYAN), new TaskBGColor(Ansi.Color.DEFAULT)),

    ALIGNMENT_LEFT("left", new TaskAlignment(ConsoleAlignment.LEFT), new TaskAlignment(ConsoleAlignment.LEFT)),
    ALIGNMENT_CENTER("center", new TaskAlignment(ConsoleAlignment.CENTER), new TaskAlignment(ConsoleAlignment.LEFT)),
    ALIGNMENT_RIGHT("right", new TaskAlignment(ConsoleAlignment.RIGHT), new TaskAlignment(ConsoleAlignment.LEFT)),

    ATTRIBUTE_INTENSITY_BOLD("intensity:bold", new TaskAttribute(Ansi.Attribute.INTENSITY_BOLD), new TaskAttribute(Ansi.Attribute.INTENSITY_BOLD_OFF)),
    ATTRIBUTE_INTENSITY_FAINT("intensity:faint", new TaskAttribute(Ansi.Attribute.INTENSITY_FAINT), new TaskAttribute(Ansi.Attribute.INTENSITY_BOLD_OFF)),
    ATTRIBUTE_ITALIC("italic", new TaskAttribute(Ansi.Attribute.ITALIC), new TaskAttribute(Ansi.Attribute.ITALIC_OFF)),
    ATTRIBUTE_UNDERLINE("underline", new TaskAttribute(Ansi.Attribute.UNDERLINE), new TaskAttribute(Ansi.Attribute.UNDERLINE_OFF)),
    ATTRIBUTE_UNDERLINE_DOUBLE("underline:double", new TaskAttribute(Ansi.Attribute.UNDERLINE_DOUBLE), new TaskAttribute(Ansi.Attribute.UNDERLINE_OFF)),
    ATTRIBUTE_NEGATIVE("negative", new TaskAttribute(Ansi.Attribute.NEGATIVE_ON), new TaskAttribute(Ansi.Attribute.NEGATIVE_OFF)),
    ATTRIBUTE_CONCEAL("conceal", new TaskAttribute(Ansi.Attribute.CONCEAL_ON), new TaskAttribute(Ansi.Attribute.CONCEAL_OFF)),
    ATTRIBUTE_STRIKETHROUGH("strikethrough", new TaskAttribute(Ansi.Attribute.STRIKETHROUGH_ON), new TaskAttribute(Ansi.Attribute.STRIKETHROUGH_OFF));

    private static ConsoleBuildAttribute[] consoleBuildAttributes = new ConsoleBuildAttribute[]{
            DEFAULT, DEFAULT_COLOR, DEFAULT_ATTRIBUTE, DEFAULT_ALIGNMENT,
            ERASE_LINE_ALL, ERASE_LINE_FORWARD, ERASE_LINE_BACKWARD,
            ERASE_SCREEN_ALL, ERASE_SCREEN_FORWARD, ERASE_SCREEN_BACKWARD,
            COLOR_BLACK, COLOR_WHITE, COLOR_RED, COLOR_GREEN, COLOR_BLUE, COLOR_YELLOW, COLOR_MAGENTA, COLOR_CYAN,
            COLOR_BLACK_BRIGHT, COLOR_WHITE_BRIGHT, COLOR_RED_BRIGHT, COLOR_GREEN_BRIGHT, COLOR_BLUE_BRIGHT, COLOR_YELLOW_BRIGHT, COLOR_MAGENTA_BRIGHT, COLOR_CYAN_BRIGHT,
            COLOR_BACK_BG, COLOR_WHITE_BG, COLOR_RED_BG, COLOR_GREEN_BG, COLOR_BLUE_BG, COLOR_YELLOW_BG, COLOR_MAGENTA_BG, COLOR_CYAN_BG,
            COLOR_BACK_BG_BRIGHT, COLOR_WHITE_BG_BRIGHT, COLOR_RED_BG_BRIGHT, COLOR_GREEN_BG_BRIGHT, COLOR_BLUE_BG_BRIGHT, COLOR_YELLOW_BG_BRIGHT, COLOR_MAGENTA_BG_BRIGHT, COLOR_CYAN_BG_BRIGHT,
            ALIGNMENT_LEFT, ALIGNMENT_CENTER, ALIGNMENT_RIGHT,
            ATTRIBUTE_INTENSITY_BOLD, ATTRIBUTE_INTENSITY_FAINT, ATTRIBUTE_ITALIC, ATTRIBUTE_UNDERLINE, ATTRIBUTE_UNDERLINE_DOUBLE, ATTRIBUTE_NEGATIVE, ATTRIBUTE_CONCEAL, ATTRIBUTE_STRIKETHROUGH
    };

    private String name;

    private ConsoleMessageTask taskOn = null;
    private ConsoleMessageTask taskOff = null;

    ConsoleBuildAttribute(String name) {
        this.name = name;
    }

    ConsoleBuildAttribute(String name, ConsoleMessageTask taskOn) {
        this.name = name;
        this.taskOn = taskOn;
    }

    ConsoleBuildAttribute(String name, ConsoleMessageTask taskOn, ConsoleMessageTask taskOff) {
        this.name = name;
        this.taskOn = taskOn;
        this.taskOff = taskOff;
    }

    public static ConsoleBuildAttribute get(String name) {
        for (ConsoleBuildAttribute buildAttribute : consoleBuildAttributes) {
            if (buildAttribute.name.equalsIgnoreCase(name)) {
                return buildAttribute;
            }
        }
        return null;
    }

    public static List<ConsoleMessageTask> getTask(String s) {
        List<ConsoleMessageTask> tasks = new ArrayList<>();

        boolean b = s.startsWith("/");
        if (b) {
            s = s.substring(1);
        }
        s = s.trim();

        if (s.toLowerCase().startsWith("color:bg(") && s.endsWith(")")) {
            s = s.substring(8, s.length() - 1);
            if (b) {
                tasks.add(new TaskBGColor(Ansi.Color.DEFAULT));
                return tasks;
            }
            if (s.length() != 6) {
                return tasks;
            }
            tasks.add(new TaskBGColorRGB(ColorUtils.getColor(s)));
            return tasks;
        }
        if (s.toLowerCase().startsWith("color(") && s.endsWith(")")) {
            s = s.substring(6, s.length() - 1);
            if (b) {
                tasks.add(new TaskColor(Ansi.Color.DEFAULT));
                return tasks;
            }
            if (s.length() != 6) {
                return tasks;
            }
            tasks.add(new TaskColorRGB(ColorUtils.getColor(s)));
            return tasks;
        }

        ConsoleBuildAttribute buildAttribute = ConsoleBuildAttribute.get(s);
        if (buildAttribute == null) {
            return tasks;
        }

        if (!b && buildAttribute.taskOn != null) {
            tasks.add(buildAttribute.taskOn);
        } else if (buildAttribute.name.equals("default")) {
            tasks.add(DEFAULT_COLOR.taskOn);
            tasks.add(DEFAULT_ATTRIBUTE.taskOn);
            tasks.add(DEFAULT_ALIGNMENT.taskOn);
        } else if (b && buildAttribute.taskOff != null) {
            tasks.add(buildAttribute.taskOff);
        }

        return tasks;
    }

}