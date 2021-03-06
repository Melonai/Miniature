package com.melonai.miniature.argument.option;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;

public interface IOption<C, A extends Annotation> {
    default boolean inputAllowedByOption(String input, A annotation) {
        return true;
    }

    default boolean constructedAllowedByOption(C constructed, A annotation) {
        return true;
    }

    @Nullable
    default String mutateInput(String originalInput, A annotation) {
        return originalInput;
    }

    @Nullable
    default C mutateConstructed(C originalConstructed, A annotation) {
        return originalConstructed;
    }
}
