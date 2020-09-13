package me.mel.miniaturebot.argument.arguments;

import me.mel.miniaturebot.argument.Argument;

public class StringArgument extends Argument {
    public StringArgument(String name, String input) {
        super(name, input);
    }

    public String get() {
        return this.getInput();
    }
}
