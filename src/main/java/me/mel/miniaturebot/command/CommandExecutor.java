package me.mel.miniaturebot.command;

import me.mel.miniaturebot.argument.Argument;
import me.mel.miniaturebot.errors.UnknownError;
import me.mel.miniaturebot.errors.UnmatchedArgumentError;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class CommandExecutor {
    private final Command command;
    private final Method runMethod;
    private final Parameter[] argumentParameters;

    public CommandExecutor(Command command, Method runMethod) {
        this.command = command;
        this.runMethod = runMethod;
        Parameter[] parameters = this.runMethod.getParameters();
        this.argumentParameters = Arrays.copyOfRange(parameters, 1, parameters.length);
    }

    public void execute(CommandContext ctx, List<Argument> arguments) throws UnknownError {
        List<Object> parameters = new ArrayList<>(arguments);
        parameters.add(0, ctx);
        try {
            runMethod.invoke(command, parameters.toArray());
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new UnknownError(e);
        }
    }

    @Nullable
    public List<Argument> constructArguments(String[] userArguments) {
        List<Argument> constructedArguments = new ArrayList<>();
        if (userArguments.length == this.argumentParameters.length) {
            for (int i = 0; i < this.argumentParameters.length; i++) {
                try {
                    Parameter parameter = this.argumentParameters[i];
                    String name = parameter.getName();
                    String input = userArguments[i];
                    Annotation[] annotations = parameter.getAnnotations();
                    Argument argument = (Argument) parameter.getType().getDeclaredConstructor(String.class, String.class, Annotation[].class).newInstance(name, input, annotations);
                    constructedArguments.add(argument);
                } catch (InstantiationException | NoSuchMethodException | IllegalAccessException e) {
                    e.printStackTrace();
                    return null;
                } catch (InvocationTargetException e) {
                    if (!(e.getTargetException() instanceof UnmatchedArgumentError)) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }
            return constructedArguments;
        }
        return null;
    }

    public static CommandExecutor[] createExecutorsForCommand(Command command) {
        return Arrays.stream(CommandExecutor.findRunMethods(command)).map(m -> new CommandExecutor(command, m)).toArray(CommandExecutor[]::new);
    }

    private static Method[] findRunMethods(Command command) {
        Method[] methods = command.getClass().getMethods();
        List<Method> foundMethods = new ArrayList<>();

        methodSweeper:
        for (Method method : methods) {
            if (method.getName().equalsIgnoreCase("run")) {

                Parameter[] parameters = method.getParameters();
                for (int i = 0; i < parameters.length; i++) {
                    Class<?> argumentType = parameters[i].getType();
                    if ((i == 0 && !argumentType.equals(CommandContext.class)) || (i > 0 && !argumentType.getSuperclass().equals(Argument.class))) {
                        continue methodSweeper;
                    }
                }

                method.setAccessible(true);
                foundMethods.add(method);
            }
        }

        foundMethods.sort(Comparator.comparingInt(Method::getParameterCount).reversed());
        return foundMethods.toArray(Method[]::new);
    }
}
