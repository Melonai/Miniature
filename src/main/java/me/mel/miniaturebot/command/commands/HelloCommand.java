package me.mel.miniaturebot.command.commands;

import me.mel.miniaturebot.command.CommandContext;
import me.mel.miniaturebot.command.CommandInfo;
import me.mel.miniaturebot.command.Command;

@CommandInfo(name = "Hello", handles = "hello")
public class HelloCommand extends Command {
    public void run(CommandContext ctx) {
        ctx.reply("Hello!");
    }
}
