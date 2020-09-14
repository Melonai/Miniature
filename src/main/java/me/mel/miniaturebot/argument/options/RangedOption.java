package me.mel.miniaturebot.argument.options;

import me.mel.miniaturebot.argument.IOption;
import me.mel.miniaturebot.util.Range;

public class RangedOption implements IOption<Integer, Ranged> {
    @Override
    public boolean constructedAllowedByOption(Integer constructed, Ranged annotation) {
        return new Range(annotation).checkFits(constructed);
    }
}
