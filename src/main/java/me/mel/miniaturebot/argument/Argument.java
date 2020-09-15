package me.mel.miniaturebot.argument;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;

public abstract class Argument<C> {
    private static final OptionHolder optionHolder = OptionHolder.getInstance();
    private final String name;
    private final String input;
    private final C constructed;

    @SuppressWarnings("unchecked")
    public Argument(String name, String input, Annotation[] annotations) throws UnmatchedArgumentException {
        this.name = name;
        String modifiedInput = input;

        Class<C> constructedClass = (Class<C>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

        for (Annotation annotation : annotations) {
            IOption option = optionHolder.getOption(constructedClass, annotation.annotationType());
            if (option.inputAllowedByOption(input, annotation)) {
                modifiedInput = option.mutateInput(modifiedInput, annotation);
            } else {
                throw new UnmatchedArgumentException("Argument couldn't be constructed.");
            }
        }

        this.input = modifiedInput;
        C modifiedConstructed = this.getConstructed();

        for (Annotation annotation : annotations) {
            IOption option = optionHolder.getOption(constructedClass, annotation.annotationType());
            if (option.constructedAllowedByOption(modifiedConstructed, annotation)) {
                modifiedConstructed = (C) option.mutateConstructed(modifiedConstructed, annotation);
            } else {
                throw new UnmatchedArgumentException("Argument couldn't be constructed.");
            }
        }

        this.constructed = modifiedConstructed;
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
