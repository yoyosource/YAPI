// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.string.commandengine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ArgumentMapping {

    private String name;
    private ArgumentMappingType type;

    private List<String> aliases = new ArrayList<>();

    private String description;

    public ArgumentMapping(String name, ArgumentMappingType type, String description) {
        this.name = name;
        this.type = type;
        this.description = description;
    }

    public ArgumentMapping(String name, ArgumentMappingType type, String description, String... aliases) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.aliases = Arrays.stream(aliases).collect(Collectors.toList());
    }

    public Argument getArgument(String value) {
        return null;
    }

}