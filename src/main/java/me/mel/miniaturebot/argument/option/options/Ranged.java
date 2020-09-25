package me.mel.miniaturebot.argument.option.options;

import me.mel.miniaturebot.argument.option.IOption;
import me.mel.miniaturebot.util.Range;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Ranged {
    int from() default Integer.MIN_VALUE;
    int to() default Integer.MAX_VALUE;

    class RangedOption implements IOption<Integer, Ranged> {
        @Override
        public boolean constructedAllowedByOption(Integer constructed, Ranged annotation) {
            return new Range(annotation).checkFits(constructed);
        }
    }
}
