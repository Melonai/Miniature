package me.mel.miniaturebot.argument;

public abstract class Argument {
    private final String name;
    private final String input;

    public Argument(String name, String input) {
        this.name = name;
        this.input = input;
    }

    public String getName() {
        return name;
    }

    public String getInput() {
        return input;
    }
}
