package me.mel.miniaturebot.command.commands;

import me.mel.miniaturebot.PrefixManager;
import me.mel.miniaturebot.argument.arguments.StringArgument;
import me.mel.miniaturebot.argument.option.options.Sized;
import me.mel.miniaturebot.command.CommandContext;
import me.mel.miniaturebot.command.CommandInfo;
import me.mel.miniaturebot.command.Command;

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
