package me.mel.miniaturebot.command;

import me.mel.miniaturebot.argument.Argument;
import me.mel.miniaturebot.argument.OptionHolder;
import me.mel.miniaturebot.argument.UnmatchedArgumentException;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandExecutor {
    private final ICommand command;
    private final Method runMethod;
    private final List<Parameter> argumentParameters;

    public CommandExecutor(ICommand command) throws Exception {
        this.command = command;

        try {
            this.runMethod = this.findRunMethod(command);
            this.runMethod.setAccessible(true);
            Parameter[] parameters = this.runMethod.getParameters();
            for (int i = 0; i < parameters.length; i++) {
                Class<?> argumentType = parameters[i].getType();
                if ((i == 0 && !argumentType.equals(CommandContext.class)) || (i > 0 && !argumentType.getSuperclass().equals(Argument.class))) {
                    throw new Exception("Run method has incorrect parameter signature.");
                }
            }
            this.argumentParameters = Arrays.asList(parameters).subList(1, parameters.length);
        } catch (NoSuchMethodException e) {
            throw new Exception("Run method not found in command.");
        }
    }

    private Method findRunMethod(ICommand command) throws NoSuchMethodException {
        Method[] methods = command.getClass().getMethods();
        for (Method method : methods) {
            if (method.getName().equalsIgnoreCase("run")) {
                return method;
            }
        }
        throw new NoSuchMethodException();
    }

    public void execute(CommandContext ctx, List<Argument> arguments) {
        List<Object> parameters = new ArrayList<>(arguments);
        parameters.add(0, ctx);
        try {
            runMethod.invoke(command, parameters.toArray());
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    public List<Argument> constructArguments(List<String> userArguments) {
        List<Argument> constructedArguments = new ArrayList<>();
        if (userArguments.size() == argumentParameters.size()) {
            for (int i = 0; i < argumentParameters.size(); i++) {
                try {
                    Parameter parameter = argumentParameters.get(i);
                    String name = parameter.getName();
                    String input = userArguments.get(i);
                    Annotation[] annotations = parameter.getAnnotations();
                    Argument argument = (Argument) parameter.getType().getDeclaredConstructor(String.class, String.class, Annotation[].class).newInstance(name, input, annotations);
                    constructedArguments.add(argument);
                } catch (InstantiationException | NoSuchMethodException | IllegalAccessException e) {
                    e.printStackTrace();
                    return null;
                } catch (InvocationTargetException e) {
                    if (!(e.getTargetException() instanceof UnmatchedArgumentException)) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }
            return constructedArguments;
        }
        return null;
    }

    public boolean goesByHandle(String handle) {
        return command.getHandles().contains(handle);
    }
}
