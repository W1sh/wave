package com.w1sh.wave;

import java.util.HashMap;
import java.util.Map;

public class Context {

    private final Map<Class<?>, Object> scope;

    private static Context context;

    private Context() {
        super();
        scope = new HashMap<>();
    }

    public static void initialize() {
        synchronized (Context.class) {
            if (context == null) {
                context = new Context();
            }
        }
    }

    public static void addComponent(Class<?> classz, Object instance) {
        context.getScope().put(classz, instance);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getComponent(Class<T> classz) {
        return (T) context.getScope().get(classz);
    }

    public Map<Class<?>, Object> getScope() {
        return scope;
    }
}
