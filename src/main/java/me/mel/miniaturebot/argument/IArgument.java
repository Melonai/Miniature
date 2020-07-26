package me.mel.miniaturebot.argument;

public interface IArgument {
    public boolean check(String argument);

    public String getName();

    default public String getDisplay() {
        return this.getName();
    }
}
