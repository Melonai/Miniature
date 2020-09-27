package com.melonai.miniature.argument;

import com.melonai.miniature.argument.option.IOption;
import com.melonai.miniature.argument.option.OptionHolder;
import com.melonai.miniature.errors.UnmatchedArgumentError;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;

public abstract class Argument<C> {
    private static final OptionHolder optionHolder = OptionHolder.getInstance();
    private final String input;
    private final C constructed;

    @SuppressWarnings("unchecked")
    public Argument(String input, Annotation[] annotations) throws UnmatchedArgumentError {
        Class<C> constructedClass = (Class<C>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];

        this.input = matchInput(input, annotations, constructedClass);
        this.constructed = matchConstructed(this.getConstructed(), annotations, constructedClass);
    }

    @SuppressWarnings("unchecked")
    private C matchConstructed(C constructed, Annotation[] annotations, Class<C> constructedClass) throws UnmatchedArgumentError {
        C modifiedConstructed = constructed;
        for (Annotation annotation : annotations) {
            IOption option = optionHolder.getOption(constructedClass, annotation.annotationType());
            if (option.constructedAllowedByOption(modifiedConstructed, annotation)) {
                modifiedConstructed = (C) option.mutateConstructed(modifiedConstructed, annotation);
            } else {
                throw new UnmatchedArgumentError();
            }
        }
        return modifiedConstructed;
    }

    @SuppressWarnings("unchecked")
    private String matchInput(String input, Annotation[] annotations, Class<C> constructedClass) throws UnmatchedArgumentError {
        String modifiedInput = input;
        for (Annotation annotation : annotations) {
            IOption option = optionHolder.getOption(constructedClass, annotation.annotationType());
            if (option.inputAllowedByOption(input, annotation)) {
                modifiedInput = option.mutateInput(modifiedInput, annotation);
            } else {
                throw new UnmatchedArgumentError();
            }
        }
        return modifiedInput;
    }

    public String getInput() {
        return input;
    }

    public C get() {
        return constructed;
    }

    protected abstract C getConstructed();
}
