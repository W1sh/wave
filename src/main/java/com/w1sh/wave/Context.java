package com.w1sh.wave;

import com.w1sh.wave.annotation.Component;

import java.util.HashMap;
import java.util.Map;

public class Context {

    private final Map<Class<?>, Object> scope;
    private final Map<String, Object> qualifierMap;

    private static Context context;

    private Context() {
        super();
        scope = new HashMap<>();
        qualifierMap = new HashMap<>();
    }

    public static void initialize() {
        synchronized (Context.class) {
            if (context == null) {
                context = new Context();
            }
        }
    }

    public static void addComponent(Class<?> clazz, Object instance) {
        String qualifierName = clazz.getName();
        if (clazz.isAnnotationPresent(Component.class)) {
            String componentName = clazz.getAnnotation(Component.class).name();
            if (!componentName.isBlank()) qualifierName = clazz.getPackageName() + "." + componentName;
        }
        context.getScope().put(clazz, instance);
        context.getQualifierMap().put(qualifierName, instance);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getComponent(Class<T> clazz) {
        return (T) context.getScope().get(clazz);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getComponent(Class<T> clazz, String name) {
        String qualifierName = clazz.getPackageName() + "." + name;
        return (T) context.getQualifierMap().get(qualifierName);
    }

    public Map<Class<?>, Object> getScope() {
        return scope;
    }

    public Map<String, Object> getQualifierMap() {
        return qualifierMap;
    }
}
