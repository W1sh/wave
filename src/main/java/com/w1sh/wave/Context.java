package com.w1sh.wave;

import com.w1sh.wave.annotation.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        final List<Class<T>> candidates = new ArrayList<>();
        for (Class<?> scopeClazz : context.getScope().keySet()) {
            if (clazz.isAssignableFrom(scopeClazz)) candidates.add((Class<T>) scopeClazz);
        }

        if (candidates.isEmpty()) {
            logger.error("No injection candidate found for class {}", clazz.getSimpleName());
            return null;
        } else if (candidates.size() > 1) {
            logger.error("Multiple injection candidates found for class {}", clazz.getSimpleName());
            // throw exception
        }

        final Object instance = context.getScope().get(candidates.get(0));
        return clazz.cast(instance);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getComponent(Class<T> clazz, String name) {
        String qualifierName = clazz.getPackageName() + "." + name;
        final T component = (T) context.getQualifierMap().getOrDefault(qualifierName, null);

        if (component == null) {
            logger.error("No injection candidates found for class {} with name {}", clazz.getSimpleName(), name);
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
