package me.mel.miniaturebot.argument.arguments;

import me.mel.miniaturebot.argument.IArgument;
import me.mel.miniaturebot.util.Range;

import javax.annotation.Nullable;
import java.util.Objects;

public class StringArgument implements IArgument {
    private final String name;
    private final Range range;

    public StringArgument(String name, Range range) {
        this.name = name;
        this.range = range;
    }

    @Override
    public boolean check(String argument) {
        return range.checkFits(argument.length());
    }

    @Override
    public String getName() {
        return name;
    }
}
