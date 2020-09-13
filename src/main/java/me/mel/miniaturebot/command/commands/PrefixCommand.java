package me.mel.miniaturebot.command.commands;

import me.mel.miniaturebot.PrefixManager;
import me.mel.miniaturebot.argument.arguments.StringArgument;
import me.mel.miniaturebot.command.CommandContext;
import me.mel.miniaturebot.command.CommandInfo;
import me.mel.miniaturebot.command.ICommand;

@CommandInfo(name = "Prefix", handles = "prefix")
public class PrefixCommand implements ICommand {
    public void run(CommandContext ctx, StringArgument newPrefix) {
        PrefixManager.setPrefixForGuild(ctx.getGuild().getId(), newPrefix.get());
        ctx.replyFormat("Your new guild prefix is now `%s`!", newPrefix.get());
    }
}
