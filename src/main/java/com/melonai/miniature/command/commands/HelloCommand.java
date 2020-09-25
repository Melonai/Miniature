package com.melonai.miniature.command.commands;

import com.melonai.miniature.command.CommandContext;
import com.melonai.miniature.command.CommandInfo;
import com.melonai.miniature.command.Command;

@CommandInfo(name = "Hello", handles = "hello")
public class HelloCommand extends Command {
    public void run(CommandContext ctx) {
        ctx.reply("Hello!");
    }
}
