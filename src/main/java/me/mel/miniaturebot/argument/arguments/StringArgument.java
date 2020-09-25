package me.mel.miniaturebot.argument.arguments;

import me.mel.miniaturebot.argument.Argument;
import me.mel.miniaturebot.errors.UnmatchedArgumentError;

import java.lang.annotation.Annotation;

public class StringArgument extends Argument<String> {
    public StringArgument(String name, String input, Annotation[] annotations) throws UnmatchedArgumentError {
        super(name, input, annotations);
    }

    @Override
    public String getConstructed() {
        return this.getInput();
    }
}
