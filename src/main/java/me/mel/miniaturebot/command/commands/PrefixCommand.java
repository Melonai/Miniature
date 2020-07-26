package me.mel.miniaturebot.command.commands;

import me.mel.miniaturebot.PrefixManager;
import me.mel.miniaturebot.argument.ArgumentFactory;
import me.mel.miniaturebot.argument.IArgument;
import me.mel.miniaturebot.command.CommandContext;
import me.mel.miniaturebot.command.ICommand;

import java.util.List;

public class PrefixCommand implements ICommand {
    @Override
    public void run(CommandContext ctx) {
        String newPrefix = ctx.getArg("newPrefix");
        PrefixManager.setPrefixForGuild(ctx.getGuild().getId(), newPrefix);
        ctx.getEvent().getChannel().sendMessageFormat("Your new guild prefix is now `%s`!", newPrefix).queue();
    }

    @Override
    public List<String> getHandles() {
        return List.of("prefix");
    }

    @Override
    public List<IArgument> getArguments() {
        return List.of(ArgumentFactory.sizedString("newPrefix", 1, 5));
    }
}
