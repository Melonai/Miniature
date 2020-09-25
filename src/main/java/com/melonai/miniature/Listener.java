package com.melonai.miniature;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

public class Listener extends ListenerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(Listener.class);
    private final CommandManager manager = new CommandManager();

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        logger.info("{} is ready!", event.getJDA().getSelfUser().getAsTag());
    }

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        User user = event.getAuthor();
        Guild guild = event.getGuild();
        String rawContent = event.getMessage().getContentRaw();
        String prefix = PrefixManager.getPrefixForGuild(guild.getId());

        if (user.isBot() || event.isWebhookMessage()) return;

        if (rawContent.startsWith(prefix)) {
            String[] words = rawContent.substring(prefix.length()).split("\\s");
            manager.handle(event, words);
        }
    }
}
