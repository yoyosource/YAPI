package yapi.manager.config.type;

public enum ConfigLoadType {
    // NONE is for Loading a Config when the program says so.
    NONE,

    // PRELOAD is for loading a Config when the program starts.
    PRELOAD,

    // REQUEST is for loading a Config when you request data from this config file.
    REQUEST
}
