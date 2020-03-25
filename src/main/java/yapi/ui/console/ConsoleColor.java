// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.ui.console;

import org.fusesource.jansi.Ansi;

public enum ConsoleColor {

    BLACK("BLACK"),
    RED("RED"),
    GREEN("GREEN"),
    YELLOW("YELLOW"),
    BLUE("BLUE"),
    MAGENTA("MAGENTA"),
    CYAN("CYAN"),
    WHITE("WHITE"),
    DEFAULT("DEFAULT");

    private Ansi.Color color;

    ConsoleColor(String name) {
        color = Ansi.Color.valueOf(name);
    }

    @Override
    public String toString() {
        return color.toString();
    }

    public Ansi.Color value() {
        return color;
    }

}