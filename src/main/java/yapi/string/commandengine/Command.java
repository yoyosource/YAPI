// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.string.commandengine;

public class Command {

    private String name;
    private ArgumentMapper argumentMapper = new ArgumentMapper();

    public Command(String name) {

    }

    public Command(String name, ArgumentMapper argumentMapper) {

    }

    public Command setArgumentMapper(ArgumentMapper argumentMapper) {
        this.argumentMapper = argumentMapper;
        return this;
    }

    public Command addMapping(ArgumentMapping argumentMapping) {
        addArgument(argumentMapping);
        return this;
    }

    public Command addArgument(ArgumentMapping argumentMapping) {
        return this;
    }

}