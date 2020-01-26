package yapi.manager.config.type;

public enum ConfigSaveType {
    // SAVEONCHANGE is for saving the Config each time when it gets changed.
    SAVEONCHANGE,

    // SAVEONREQUEST is for saving the Config each time the program request the ConfigManager to do so.
    SAVEONREQUEST,

    // NOSAVE is for having a Config which gets created once and will not change afterwards.
    NOSAVE,

    // DELETEONEXIT is for having a temporary Config which gets deleted after existing the program.
    DELETEONEXIT
}
