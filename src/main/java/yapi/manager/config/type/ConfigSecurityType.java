package yapi.manager.config.type;

public enum ConfigSecurityType {
    // NONE is for saving the Config as plain text.
    NONE,

    // PASSWORD is for saving the Config with a password. You need to give the ConfigManager the Password each time you start the program or when you first load this Config.
    PASSWORD,

    // BOUNDTOPC is for saving and loading a PC specific Config. This ensures no easy exchange of configs.
    BOUNDTOPC
}
