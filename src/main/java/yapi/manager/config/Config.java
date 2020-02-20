// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.manager.config;

import yapi.internal.exceptions.config.ConfigFileExistsException;
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
    String password;
    ConfigFileType configFileType;
    ConfigSecurityType configSecurityType;
    ConfigLoadType configLoadType;
    ConfigSaveType configSaveType;

    private String fileSuffix = ".yconfig";

    public Config(String name, String password, ConfigFileType configFileType, ConfigSecurityType configSecurityType, ConfigLoadType configLoadType, ConfigSaveType configSaveType) {
        this.name = FileUtils.getName(name);
        this.password = password;
        this.configFileType = configFileType;
        this.configSecurityType = configSecurityType;
        this.configLoadType = configLoadType;
        this.configSaveType = configSaveType;
    }

    boolean checkState(String path) {
        return getFile(path).exists();
    }

    File getFile(String path) {
        return new File(path + File.separator + name + fileSuffix);
    }

    boolean createConfig(String path) throws IOException {
        if (checkState(path)) {
            throw new ConfigFileExistsException();
        }
        File f = getFile(path);
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
                FileUtils.dump(getFile(path), yapionObject.toJson().toString());
            } else if (configFileType == ConfigFileType.YAPION) {
                FileUtils.dump(getFile(path), yapionObject.toString());
            } else if (configFileType == ConfigFileType.YAPIONHIERARCHY) {
                FileUtils.dump(getFile(path), yapionObject.toHierarchyString());
            }
        } else if (configSecurityType == ConfigSecurityType.PASSWORD) {

        }
    }
}