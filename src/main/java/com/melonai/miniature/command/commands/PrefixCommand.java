package com.melonai.miniature.command.commands;

import com.melonai.miniature.argument.arguments.StringArgument;
import com.melonai.miniature.argument.option.options.Sized;
import com.melonai.miniature.command.CommandContext;
import com.melonai.miniature.PrefixManager;
import com.melonai.miniature.command.CommandInfo;
import com.melonai.miniature.command.Command;

@CommandInfo(name = "Prefix", handles = "prefix")
public class PrefixCommand extends Command {
    public void run(CommandContext ctx, @Sized(to = 5) StringArgument newPrefix) {
        PrefixManager.setPrefixForGuild(ctx.getGuild().getId(), newPrefix.get());
        ctx.replyFormat("Your new guild prefix is now `%s`!", newPrefix.get());
    }

    public void run(CommandContext ctx) {
        ctx.replyFormat("Your current guild prefix is `%s`.", PrefixManager.getPrefixForGuild(ctx.getGuild().getId()));
    }
}
