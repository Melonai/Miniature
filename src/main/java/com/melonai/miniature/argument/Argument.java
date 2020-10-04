package com.melonai.miniature.argument;

import com.melonai.miniature.argument.option.IOption;
import com.melonai.miniature.argument.option.OptionHolder;
import com.melonai.miniature.errors.UnmatchedArgumentError;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;

public abstract class Argument<C> implements RawArgument {
    private static final OptionHolder optionHolder = OptionHolder.getInstance();
    private final String input;
    private final C constructed;
    private final Class<C> constructedClass;

    public Argument(String input, Annotation[] annotations) throws UnmatchedArgumentError {
        this.constructedClass = this.getClassOfGenericTypeArgument();

        this.input = matchInput(input, annotations);
        this.constructed = matchConstructed(this.getConstructed(), annotations);
    }

    @SuppressWarnings("unchecked")
    private Class<C> getClassOfGenericTypeArgument() {
        return (Class<C>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @SuppressWarnings("unchecked")
    private C matchConstructed(C constructed, Annotation[] annotations) throws UnmatchedArgumentError {
        C modifiedConstructed = constructed;
        for (Annotation annotation : annotations) {
            IOption<C, Annotation> option = (IOption<C, Annotation>) optionHolder.getOption(this.constructedClass, annotation.annotationType());
            if (option.constructedAllowedByOption(modifiedConstructed, annotation)) {
                modifiedConstructed = option.mutateConstructed(modifiedConstructed, annotation);
            } else {
                throw new UnmatchedArgumentError();
            }
        }
        return modifiedConstructed;
    }

    private String matchInput(String input, Annotation[] annotations) throws UnmatchedArgumentError {
        String modifiedInput = input;
        for (Annotation annotation : annotations) {
            IOption<?, Annotation> option = optionHolder.getOption(this.constructedClass, annotation.annotationType());
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
