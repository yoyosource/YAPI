// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.os;

public enum OSType {

    MAC_OS("MacOs"),
    LINUX("Linux"),
    WINDOWS("Windows"),
    OTHER("");

    private String name;

    OSType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}