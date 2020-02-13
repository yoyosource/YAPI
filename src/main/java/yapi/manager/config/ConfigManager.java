// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.manager.config;

public class ConfigManager {

    private static ConfigManager configManager;

    static {
        configManager = new ConfigManager();
    }

    private ConfigManager() {

    }

    public static ConfigManager getInstance() {
        return configManager;
    }

    public static ConfigManager getConfigManager() {
        return getInstance();
    }

}