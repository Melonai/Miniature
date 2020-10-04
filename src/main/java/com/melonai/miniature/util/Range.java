package com.melonai.miniature.util;

import com.melonai.miniature.argument.option.options.Ranged;
import com.melonai.miniature.argument.option.options.Sized;

import javax.annotation.Nullable;

public class Range {
    @Nullable
    private final Integer lower;
    @Nullable
    private final Integer upper;

    public Range(@Nullable Integer lower, @Nullable Integer upper) {
        this.lower = lower;
        this.upper = upper;
    }

    public Range(Sized annotation) {
        this.lower = annotation.from();
        this.upper = annotation.to();
    }

    public Range(Ranged annotation) {
        this.lower = annotation.from();
        this.upper = annotation.to();
    }

    public boolean checkFits(Integer toCheck) {
        return ((lower == null || lower <= toCheck) && (upper == null || upper >= toCheck));
    }
}
