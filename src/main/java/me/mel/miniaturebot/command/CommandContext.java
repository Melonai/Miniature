package me.mel.miniaturebot.command;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.HashMap;

public class CommandContext {
    private final GuildMessageReceivedEvent event;
    private final HashMap<String, String> args;

    public CommandContext(GuildMessageReceivedEvent event, HashMap<String, String> args) {
        this.event = event;
        this.args = args;
    }

    public Guild getGuild() {
        return this.event.getGuild();
    }

    public GuildMessageReceivedEvent getEvent() {
        return this.event;
    }

    public String getArg(String key) {
        return this.args.get(key);
    }
}
