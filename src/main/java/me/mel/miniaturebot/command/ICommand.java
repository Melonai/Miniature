package me.mel.miniaturebot.command;

import me.mel.miniaturebot.argument.IArgument;

import java.util.Arrays;
import java.util.List;

public interface ICommand {
    void run(CommandContext ctx);

    default List<String> getHandles() {
        return Arrays.asList(this.getClass().getAnnotation(CommandInfo.class).handles());
    }

    default String getName() {
        return this.getClass().getAnnotation(CommandInfo.class).name();
    }

    default List<IArgument> getArguments() {
        return List.of();
    }
}
