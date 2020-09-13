package me.mel.miniaturebot.command.commands;

import me.mel.miniaturebot.command.CommandContext;
import me.mel.miniaturebot.command.CommandInfo;
import me.mel.miniaturebot.command.ICommand;

@CommandInfo(name = "Hello", handles = "hello")
public class HelloCommand implements ICommand {
    public void run(CommandContext ctx) {
        ctx.reply("Hello!");
    }
}
