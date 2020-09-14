package me.mel.miniaturebot.argument.arguments;

import me.mel.miniaturebot.argument.Argument;
import me.mel.miniaturebot.argument.UnmatchedArgumentException;

import java.lang.annotation.Annotation;

public class StringArgument extends Argument<String> {
    public StringArgument(String name, String input, Annotation[] annotations) throws UnmatchedArgumentException {
        super(name, input, annotations);
    }

    @Override
    public String getConstructed() {
        return this.getInput();
    }
}
