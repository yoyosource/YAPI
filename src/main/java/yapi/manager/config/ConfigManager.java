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
