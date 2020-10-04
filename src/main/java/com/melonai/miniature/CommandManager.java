package com.melonai.miniature;

import com.melonai.miniature.command.CommandContext;
import com.melonai.miniature.command.Command;
import com.melonai.miniature.errors.UnknownError;
import com.melonai.miniature.errors.UnmatchedArgumentError;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class CommandManager {
    private final List<Command> commands = new ArrayList<>();
    private static final Logger logger = LoggerFactory.getLogger(Listener.class);

    public CommandManager() {
        Set<Class<? extends Command>> foundCommands = CommandManager.findCommandsWithReflection();
        foundCommands.forEach((command) -> {
            try {
                addCommand(command.getDeclaredConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        });
        logger.info("{} commands were loaded.", this.commands.size());
    }

    private static Set<Class<? extends Command>> findCommandsWithReflection() {
        Reflections reflections = new Reflections(CommandManager.class.getPackageName());
        return reflections.getSubTypesOf(Command.class);
    }

    private void addCommand(Command newCommand) {
        boolean handleOverlap = newCommand.getHandles().stream().anyMatch((handle) -> findCommandByHandle(handle) != null);

        if (handleOverlap) {
            throw new IllegalArgumentException("One or more handles of the new command are already occupied.");
        }

        try {
            commands.add(newCommand);
        } catch (Exception e) {
            LoggerFactory.getLogger(newCommand.getClass()).error(e.getMessage());
        }
    }

    @Nullable
    private Command findCommandByHandle(String handle) {
        for (Command command : this.commands) {
            if (command.goesByHandle(handle)) {
                return command;
            }
        }
        return null;
    }

    public void handle(GuildMessageReceivedEvent event, String[] words) {
        String handle = words[0];
        Command command = this.findCommandByHandle(handle);
        if (command != null) {
            String[] userArguments = Arrays.copyOfRange(words, 1, words.length);
            CommandContext ctx = new CommandContext(event);
            try {
                command.execute(ctx, userArguments);
            } catch (UnknownError unknownError) {
                ctx.reply("Something unexpected happened.");
            } catch (UnmatchedArgumentError unmatchedArgumentError) {
                ctx.reply("Please check command usage.");
            }
        }
    }
}
