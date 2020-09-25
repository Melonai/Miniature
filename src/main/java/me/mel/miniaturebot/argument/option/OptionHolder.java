package me.mel.miniaturebot.argument.option;

import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public class OptionHolder {
    private static final OptionHolder instance = new OptionHolder();
    private final HashMap<Class<? extends Annotation>, List<IOption>> map;

    @SuppressWarnings("unchecked")
    private OptionHolder() {
        this.map = new HashMap<>();
        Reflections reflections = new Reflections(OptionHolder.class.getPackageName());
        Set<Class<? extends IOption>> options = reflections.getSubTypesOf(IOption.class);
        options.forEach((optionClass) -> {
            ParameterizedType generic = (ParameterizedType) optionClass.getGenericInterfaces()[0];
            Type[] typeArguments =  generic.getActualTypeArguments();
            Class<? extends Annotation> annotationClass = (Class<? extends Annotation>) typeArguments[1];

            try {
                IOption option = optionClass.getDeclaredConstructor().newInstance();
                this.map.putIfAbsent(annotationClass, new ArrayList<>());
                this.map.get(annotationClass).add(option);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        });
    }

    public <C, A> IOption getOption(C constructedType, A annotationType) {
        List<IOption> handlers = this.map.get(annotationType);

        for (IOption handler : handlers) {
            Class<?> constructedExpected = (Class<?>) ((ParameterizedType) handler.getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0];
            if (constructedExpected.isAssignableFrom(constructedType.getClass())) {
                return handler;
            }
        }

        throw new IllegalArgumentException(String.format("%s does not support type %s.", annotationType, constructedType));
    }

    public static OptionHolder getInstance() {
        return instance;
    }
}
