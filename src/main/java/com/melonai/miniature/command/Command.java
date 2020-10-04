package com.melonai.miniature.command;

import com.melonai.miniature.argument.RawArgument;
import com.melonai.miniature.errors.UnknownError;
import com.melonai.miniature.errors.UnmatchedArgumentError;

import java.util.Arrays;
import java.util.List;

public abstract class Command {
    private final CommandExecutor[] executors;

    public Command() {
        this.executors = CommandExecutor.createExecutorsForCommand(this);
    }

    public void execute(CommandContext ctx, String[] userArguments) throws UnknownError, UnmatchedArgumentError {
        for (CommandExecutor executor : executors) {
            List<RawArgument> constructedArguments = executor.constructArguments(userArguments);
            if (constructedArguments != null) {
                try {
                    executor.execute(ctx, constructedArguments);
                    return;
                } catch (UnknownError e) {
                    e.getCause().printStackTrace();
                    throw e;
                }
            }
        }
        throw new UnmatchedArgumentError();
    }

    public List<String> getHandles() {
        return Arrays.asList(this.getClass().getAnnotation(CommandInfo.class).handles());
    }

    public String getName() {
        return this.getClass().getAnnotation(CommandInfo.class).name();
    }

    public boolean goesByHandle(String handle) {
        return this.getHandles().contains(handle);
    }
}
