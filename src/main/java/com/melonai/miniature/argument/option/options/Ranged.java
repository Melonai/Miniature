package com.melonai.miniature.argument.option.options;

import com.melonai.miniature.argument.option.IOption;
import com.melonai.miniature.util.Range;

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
