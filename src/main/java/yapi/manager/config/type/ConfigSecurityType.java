// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.manager.config.type;

public enum ConfigSecurityType {
    // NONE is for saving the Config as plain text.
    NONE,

    // PASSWORD is for saving the Config with a password. You need to give the ConfigManager the Password each time you start the program or when you first load this Config.
    PASSWORD
}