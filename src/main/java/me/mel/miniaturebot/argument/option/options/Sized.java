package me.mel.miniaturebot.argument.option.options;

import me.mel.miniaturebot.argument.option.IOption;
import me.mel.miniaturebot.util.Range;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Sized {
    int from() default 1;
    int to() default Integer.MAX_VALUE;

    class SizedOption implements IOption<Object, Sized> {
        @Override
        public boolean inputAllowedByOption(String input, Sized annotation) {
            return new Range(annotation).checkFits(input.length());
        }
    }
}
