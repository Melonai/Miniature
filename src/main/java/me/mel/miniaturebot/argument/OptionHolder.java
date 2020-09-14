package me.mel.miniaturebot.argument;

import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Set;

public class OptionHolder {
    private static final OptionHolder instance = new OptionHolder();
    private final HashMap<Class<? extends Annotation>, IOption> map;

    @SuppressWarnings("unchecked")
    private OptionHolder() {
        this.map = new HashMap<>();
        Reflections reflections = new Reflections(OptionHolder.class.getPackageName());
        Set<Class<? extends IOption>> options = reflections.getSubTypesOf(IOption.class);
        options.forEach((option) -> {
            ParameterizedType generic = (ParameterizedType) option.getGenericInterfaces()[0];
            Class<? extends Annotation> annotationClass = (Class<? extends Annotation>) generic.getActualTypeArguments()[1];
            try {
                this.map.put(annotationClass, option.getDeclaredConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        });
    }

    @SuppressWarnings("unchecked")
    public <C, A> IOption<C, A> getOption(C constructedType, A annotationType) {
        IOption option = this.map.get(annotationType);
        ParameterizedType generic = (ParameterizedType) option.getClass().getGenericInterfaces()[0];
        if (((Class<?>) generic.getActualTypeArguments()[0]).isAssignableFrom((Class<?>) constructedType)) {
            return (IOption<C, A>) option;
        } else {
            throw new IllegalArgumentException(String.format("%s can't handle arguments of type %s.", option.getClass().toString(), constructedType.toString()));
        }
    }

    public static OptionHolder getInstance() {
        return instance;
    }
}
