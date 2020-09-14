package me.mel.miniaturebot.util;

import me.mel.miniaturebot.argument.options.Ranged;
import me.mel.miniaturebot.argument.options.Sized;

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

    public static Range fromLower(Integer lower) {
        return new Range(lower, null);
    }

    public static Range toHigher(Integer higher) {
        return new Range(null, higher);
    }

    public static Range any() {
        return new Range(null, null);
    }

    public boolean checkFits(Integer toCheck) {
        return ((lower == null || lower <= toCheck) && (upper == null || upper >= toCheck));
    }
}
