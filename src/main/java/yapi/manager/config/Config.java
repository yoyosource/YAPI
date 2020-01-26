package yapi.manager.config;

import yapi.exceptions.config.ConfigFileExistsException;
import yapi.manager.config.type.ConfigFileType;
import yapi.manager.config.type.ConfigLoadType;
import yapi.manager.config.type.ConfigSaveType;
import yapi.manager.config.type.ConfigSecurityType;
import yapi.manager.yapion.value.YAPIONObject;
import yapi.file.FileUtils;

import java.io.File;
import java.io.IOException;

public class Config {

    String name;
    ConfigFileType configFileType;
    ConfigSecurityType configSecurityType;
    ConfigLoadType configLoadType;
    ConfigSaveType configSaveType;

    public Config(String name, ConfigFileType configFileType, ConfigSecurityType configSecurityType, ConfigLoadType configLoadType, ConfigSaveType configSaveType) {
        this.name = FileUtils.getName(name);
        this.configFileType = configFileType;
        this.configSecurityType = configSecurityType;
        this.configLoadType = configLoadType;
        this.configSaveType = configSaveType;
    }

    boolean checkState(String path) {
        return new File(path + "/" + name + ".config").exists();
    }

    boolean createConfig(String path) throws IOException {
        if (checkState(path)) {
            throw new ConfigFileExistsException();
        }
        File f = new File(path + "/" + name + ".config");
        boolean b = f.createNewFile();
        if (configSaveType == ConfigSaveType.DELETEONEXIT) {
            f.deleteOnExit();
        }
        return b;
    }

    void save(String path, YAPIONObject yapionObject) throws IOException {
        if (configSaveType == ConfigSaveType.NOSAVE) {
            return;
        }
        if (configSecurityType == ConfigSecurityType.NONE) {
            if (configFileType == ConfigFileType.JSON) {
                FileUtils.dump(new File(path + "/" + name + ".config"), yapionObject.toJson().toString());
            } else if (configFileType == ConfigFileType.YAPION) {
                FileUtils.dump(new File(path + "/" + name + ".config"), yapionObject.toString());
            } else if (configFileType == ConfigFileType.YAPIONHIERARCHY) {
                FileUtils.dump(new File(path + "/" + name + ".config"), yapionObject.toHierarchyString());
            }
        } else if (configSecurityType == ConfigSecurityType.PASSWORD) {

        } else if (configSecurityType == ConfigSecurityType.BOUNDTOPC) {

        }
    }
}
