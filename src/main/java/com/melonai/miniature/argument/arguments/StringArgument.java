package com.melonai.miniature.argument.arguments;

import com.melonai.miniature.errors.UnmatchedArgumentError;
import com.melonai.miniature.argument.Argument;

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
