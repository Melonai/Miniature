package me.mel.miniaturebot.argument.options;

import me.mel.miniaturebot.argument.IOption;
import me.mel.miniaturebot.util.Range;

public class SizedOption implements IOption<Object, Sized> {
    @Override
    public boolean inputAllowedByOption(String input, Sized annotation) {
        return new Range(annotation).checkFits(input.length());
    }
}
