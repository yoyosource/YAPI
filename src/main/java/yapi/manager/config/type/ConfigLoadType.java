// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.manager.config.type;

public enum ConfigLoadType {
    // NONE is for Loading a Config when the program says so.
    NONE,

    // PRELOAD is for loading a Config when the program starts.
    PRELOAD,

    // REQUEST is for loading a Config when you request data from this config file.
    REQUEST
}