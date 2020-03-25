// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.ui.console;

import org.fusesource.jansi.Ansi;

public enum ConsoleAttribute {

    RESET("RESET"),
    INTENSITY_BOLD("INTENSITY_BOLD"),
    INTENSITY_FAINT("INTENSITY_FAINT"),
    ITALIC("ITALIC_ON"),
    UNDERLINE("UNDERLINE_ON"),
    BLINK_SLOW("BLINK_SLOW"),
    BLINK_FAST("BLINK_FAST"),
    NEGATIVE_ON("NEGATIVE_ON"),
    CONCEAL_ON("CONCEAL_ON"),
    STRIKETHROUGH_ON("STRIKETHROUGH_ON"),
    UNDERLINE_DOUBLE("UNDERLINE_DOUBLE"),
    INTENSITY_BOLD_OFF("INTENSITY_BOLD_OFF"),
    ITALIC_OFF("ITALIC_OFF"),
    UNDERLINE_OFF("UNDERLINE_OFF"),
    BLINK_OFF("BLINK_OFF"),
    NEGATIVE_OFF("NEGATIVE_OFF"),
    CONCEAL_OFF("CONCEAL_OFF"),
    STRIKETHROUGH_OFF("STRIKETHROUGH_OFF");

    private Ansi.Attribute attribute;

    ConsoleAttribute(String name) {
        attribute = Ansi.Attribute.valueOf(name);
    }

    @Override
    public String toString() {
        return attribute.toString();
    }

    public Ansi.Attribute value() {
        return attribute;
    }

}