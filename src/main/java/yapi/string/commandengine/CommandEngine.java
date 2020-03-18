package yapi.string.commandengine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommandEngine {

    private boolean generateHelpCommand = true;

    private List<Command> commands = new ArrayList<>();
    private List<CommandGroup> commandGroups = new ArrayList<>();

    public CommandEngine() {

    }

    public CommandEngine(Command... commands) {
        this.commands = Arrays.stream(commands).collect(Collectors.toList());
    }

    public CommandEngine(CommandGroup... commandGroups) {
        this.commandGroups = Arrays.stream(commandGroups).collect(Collectors.toList());
    }

    public CommandEngine addCommand(Command command) {
        commands.add(command);
        return this;
    }

    public CommandEngine addCommand(CommandGroup commandGroup) {
        commandGroups.add(commandGroup);
        return this;
    }

    public void setGenerateHelpCommand(boolean generateHelpCommand) {
        this.generateHelpCommand = generateHelpCommand;
    }

}
