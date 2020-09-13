package me.mel.miniaturebot;

import me.mel.miniaturebot.argument.Argument;
import me.mel.miniaturebot.command.CommandContext;
import me.mel.miniaturebot.command.CommandExecutor;
import me.mel.miniaturebot.command.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class CommandManager {
    private final List<CommandExecutor> commands = new ArrayList<>();
    private static final Logger logger = LoggerFactory.getLogger(Listener.class);

    public CommandManager() {
        Reflections reflections = new Reflections(CommandManager.class.getPackageName());
        Set<Class<? extends ICommand>> foundCommands = reflections.getSubTypesOf(ICommand.class);
        foundCommands.forEach((command) -> {
            try {
                addCommand(command.getDeclaredConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        });
        logger.info("{} commands were loaded.", this.commands.size());
    }

    private void addCommand(ICommand newCommand) {
        boolean handleOverlap = newCommand.getHandles().stream().anyMatch((handle) -> findCommandByHandle(handle) != null);

        if (handleOverlap) {
            throw new IllegalArgumentException("One or more handles of the new command are already occupied.");
        }

        try {
            commands.add(new CommandExecutor(newCommand));
        } catch (Exception e) {
            LoggerFactory.getLogger(newCommand.getClass()).error(e.getMessage());
        }
    }

    @Nullable
    private CommandExecutor findCommandByHandle(String handle) {
        for (CommandExecutor executor : this.commands) {
            if (executor.goesByHandle(handle)) {
                return executor;
            }
        }
        return null;
    }

    public void handle(GuildMessageReceivedEvent event, String[] words) {
        String handle = words[0];
        CommandExecutor command = this.findCommandByHandle(handle);
        if (command != null) {
            List<String> userArguments = Arrays.asList(words).subList(1, words.length);
            List<Argument> matchedArguments = command.matchArguments(userArguments);
            if (matchedArguments != null) {
                CommandContext ctx = new CommandContext(event);
                command.execute(ctx, matchedArguments);
            }
        }
    }
}
