package me.mel.miniaturebot.argument.options;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Ranged {
    int from() default Integer.MIN_VALUE;
    int to() default Integer.MAX_VALUE;
}
