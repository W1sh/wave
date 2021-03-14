package com.w1sh.wave;

import com.w1sh.wave.annotation.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class Context {

    private static final Logger logger = LoggerFactory.getLogger(Context.class);

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
        final T component = (T) context.getScope().get(clazz);

        if (component == null) {
            logger.error("No component found for class {}", clazz.getSimpleName());
        }
        return component;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getComponent(Class<T> clazz, String name) {
        String qualifierName = clazz.getPackageName() + "." + name;
        final T component = (T) context.getQualifierMap().getOrDefault(qualifierName, null);

        if (component == null) {
            logger.error("No component found for class {} and name {}", clazz.getSimpleName(), name);
        }
        return component;
    }

    public Map<Class<?>, Object> getScope() {
        return scope;
    }

    public Map<String, Object> getQualifierMap() {
        return qualifierMap;
    }
}
