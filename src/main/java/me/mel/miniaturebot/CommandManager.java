package me.mel.miniaturebot;

import me.mel.miniaturebot.argument.IArgument;
import me.mel.miniaturebot.command.CommandContext;
import me.mel.miniaturebot.command.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class CommandManager {
    private final List<ICommand> commands = new ArrayList<>();
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

        commands.add(newCommand);
    }

    @Nullable
    private ICommand findCommandByHandle(String handle) {
        for (ICommand command : this.commands) {
            if (command.getHandles().contains(handle)) {
                return command;
            }
        }
        return null;
    }

    @Nullable
    private HashMap<String, String> checkArguments(List<String> givenArguments, List<IArgument> neededArguments) {
        if (neededArguments.size() == givenArguments.size()) {
            int argumentAmount = neededArguments.size();
            HashMap<String, String> cleanArguments = new HashMap<>();
            for (int i = 0; i < argumentAmount; i++) {
                IArgument currentNeededArgument = neededArguments.get(i);
                String currentGivenArgument = givenArguments.get(i);
                if (currentNeededArgument.check(currentGivenArgument)) {
                    cleanArguments.put(currentNeededArgument.getName(), currentGivenArgument);
                } else {
                    return null;
                }
            }
            return cleanArguments;
        }
        return null;
    }

    public void handle(GuildMessageReceivedEvent event, String[] words) {
        String handle = words[0];
        ICommand foundCommand = this.findCommandByHandle(handle);
        if (foundCommand != null) {
            List<String> givenArguments = Arrays.asList(words).subList(1, words.length);
            HashMap<String, String> argumentMap = this.checkArguments(givenArguments, foundCommand.getArguments());
            if (argumentMap != null) {
                CommandContext ctx = new CommandContext(event, argumentMap);
                foundCommand.run(ctx);
            }
        }
    }
}
