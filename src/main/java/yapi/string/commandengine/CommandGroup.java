// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.string.commandengine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommandGroup {

    private String name;
    private List<Command> commands = new ArrayList<>();
    private List<CommandGroup> commandGroups = new ArrayList<>();

    public CommandGroup(String name) {
        this.name = name;
    }

    public CommandGroup(String name, Command... commands) {
        this.name = name;
        this.commands = Arrays.stream(commands).collect(Collectors.toList());
    }

    public CommandGroup(String name, CommandGroup... commandGroups) {
        this.name = name;
        this.commandGroups = Arrays.stream(commandGroups).collect(Collectors.toList());
    }

    public CommandGroup addCommand(Command command) {
        commands.add(command);
        return this;
    }

    public CommandGroup addCommand(CommandGroup commandGroup) {
        commandGroups.add(commandGroup);
        return this;
    }

}