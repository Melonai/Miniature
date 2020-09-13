package me.mel.miniaturebot.command;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class CommandContext {
    private final GuildMessageReceivedEvent event;

    public CommandContext(GuildMessageReceivedEvent event) {
        this.event = event;
    }

    public Guild getGuild() {
        return this.event.getGuild();
    }

    public GuildMessageReceivedEvent getEvent() {
        return this.event;
    }

    public void reply(String message) {
        this.event.getChannel().sendMessage(message).queue();
    }

    public void replyFormat(String format, Object... args) {
        this.event.getChannel().sendMessageFormat(format, args).queue();
    }
}
