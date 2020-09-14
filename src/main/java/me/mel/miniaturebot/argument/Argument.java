package me.mel.miniaturebot.argument;

import java.lang.annotation.Annotation;

public abstract class Argument<C> {
    private static final OptionHolder optionHolder = OptionHolder.getInstance();
    private final String name;
    private final String input;
    private final C constructed;

    @SuppressWarnings("unchecked")
    public Argument(String name, String input, Annotation[] annotations) throws UnmatchedArgumentException {
        this.name = name;
        String modifiedInput = input;

        for (Annotation annotation : annotations) {
            IOption option = optionHolder.getOption(Object.class, annotation.annotationType());
            if (option.inputAllowedByOption(input, annotation)) {
                modifiedInput = option.mutateInput(modifiedInput, annotation);
            } else {
                throw new UnmatchedArgumentException("Argument couldn't be constructed.");
            }
        }

        this.input = modifiedInput;
        C constructed = this.getConstructed();

        for (Annotation annotation : annotations) {
            IOption option = optionHolder.getOption(constructed.getClass(), annotation.annotationType());
            if (option.constructedAllowedByOption(constructed, annotation)) {
                constructed = (C) option.mutateConstructed(constructed, annotation);
            } else {
                throw new UnmatchedArgumentException("Argument couldn't be constructed.");
            }
        }

        this.constructed = constructed;
    }

    public String getName() {
        return name;
    }

    public String getInput() {
        return input;
    }

    public C get() {
        return constructed;
    }

    protected abstract C getConstructed();
}
