package me.mel.miniaturebot.argument;

import javax.annotation.Nullable;

public interface IOption<C, A> {
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
