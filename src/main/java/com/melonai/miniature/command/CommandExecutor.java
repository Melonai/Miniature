package com.melonai.miniature.command;

import com.melonai.miniature.errors.UnmatchedArgumentError;
import com.melonai.miniature.argument.Argument;
import com.melonai.miniature.errors.UnknownError;

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
                Argument argument = makeArgument(userArguments[i], this.argumentParameters[i]);
                if (argument != null) {
                    constructedArguments.add(argument);
                } else {
                    return null;
                }
            }
            return constructedArguments;
        }
        return null;
    }

    private Argument makeArgument(String input, Parameter parameter) {
        try {
            Annotation[] argumentChecks = parameter.getAnnotations();
            return (Argument) parameter.getType().getDeclaredConstructor(String.class, Annotation[].class).newInstance(input, argumentChecks);
        } catch (InstantiationException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            if (!(e.getTargetException() instanceof UnmatchedArgumentError)) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static CommandExecutor[] createExecutorsForCommand(Command command) {
        return Arrays.stream(CommandExecutor.findRunMethods(command)).map(m -> new CommandExecutor(command, m)).toArray(CommandExecutor[]::new);
    }

    private static Method[] findRunMethods(Command command) {
        Method[] allMethods = command.getClass().getMethods();
        List<Method> foundMethods = new ArrayList<>();

        for (Method method : allMethods) {
            if (CommandExecutor.isMethodRunMethod(method)) {
                method.setAccessible(true);
                foundMethods.add(method);
            }
        }

        CommandExecutor.sortRunMethodsByPriority(foundMethods);
        return foundMethods.toArray(Method[]::new);
    }

    private static boolean isMethodRunMethod(Method method) {
        if (method.getName().equalsIgnoreCase("run")) {
            Parameter[] parameters = method.getParameters();
            for (int i = 0; i < parameters.length; i++) {
                Class<?> argumentType = parameters[i].getType();
                if ((i == 0 && !argumentType.equals(CommandContext.class)) || (i > 0 && !argumentType.getSuperclass().equals(Argument.class))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private static void sortRunMethodsByPriority(List<Method> methods) {
        methods.sort(Comparator.comparingInt(Method::getParameterCount).reversed());
    }
}
