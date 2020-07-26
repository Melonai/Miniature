package me.mel.miniaturebot.command;

import me.mel.miniaturebot.argument.IArgument;

import java.util.List;

public interface ICommand {
    void run(CommandContext ctx);

    List<String> getHandles();

    default List<IArgument> getArguments() {
        return List.of();
    }
}
