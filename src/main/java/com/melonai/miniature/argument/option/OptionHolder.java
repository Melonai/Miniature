package com.melonai.miniature.argument.option;

import com.melonai.miniature.argument.option.options.Ranged;
import com.melonai.miniature.argument.option.options.Sized;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.util.*;

public class OptionHolder {
    private static final OptionHolder instance = new OptionHolder();
    private final Map<Class<? extends Annotation>, IOption<?, Annotation>[]> map;

    @SuppressWarnings("unchecked")
    public OptionHolder() {
        this.map = new HashMap<>(){{
            put(Ranged.class, new IOption[]{new Ranged.RangedOption()});
            put(Sized.class, new IOption[]{new Sized.SizedOption()});
        }};
    }

    public IOption<?, Annotation> getOption(Class<?> constructedType, Class<? extends Annotation> annotationType) {
        IOption<?, Annotation>[] handlers = this.map.get(annotationType);

        for (IOption<?, Annotation> handler : handlers) {
            Class<?> constructedExpected = (Class<?>) ((ParameterizedType) handler.getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0];
            if (constructedExpected.isAssignableFrom(constructedType)) {
                return handler;
            }
        }

        throw new IllegalArgumentException(String.format("%s does not support type %s.", annotationType, constructedType));
    }

    public static OptionHolder getInstance() {
        return instance;
    }
}
