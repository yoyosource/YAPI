// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.ui.console;

import org.fusesource.jansi.Ansi;

public enum ConsoleErase {

    FORWARD("FORWARD"),
    BACKWARD("BACKWARD"),
    ALL("ALL");

    private Ansi.Erase erase;

    ConsoleErase(String name) {
        erase = Ansi.Erase.valueOf(name);
    }

    @Override
    public String toString() {
        return erase.toString();
    }

    public Ansi.Erase value() {
        return erase;
    }

}