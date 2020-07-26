package me.mel.miniaturebot.argument;

import me.mel.miniaturebot.argument.arguments.StringArgument;
import me.mel.miniaturebot.util.Range;

public class ArgumentFactory {
    public static IArgument string(String name) {
        return new StringArgument(name, Range.fromLower(1));
    }

    public static IArgument sizedString(String name, Integer lower, Integer higher) {
        return new StringArgument(name, new Range(lower, higher));
    }
}
