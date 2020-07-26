package me.mel.miniaturebot.command.commands;

import me.mel.miniaturebot.command.CommandContext;
import me.mel.miniaturebot.command.ICommand;

import java.util.List;

public class HelloCommand implements ICommand {
    @Override
    public void run(CommandContext ctx) {
        ctx.getEvent().getChannel().sendMessage("Hello!").queue();
    }

    @Override
    public List<String> getHandles() {
        return List.of("hello");
    }
}
