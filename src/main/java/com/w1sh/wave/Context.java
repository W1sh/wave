package com.w1sh.wave;

import com.w1sh.wave.annotation.Component;
import com.w1sh.wave.annotation.Primary;
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
        if (context.getScope().containsKey(clazz)) {
            logger.warn("Instance of class {} is already present in the context", clazz);
            return;
        }
        String qualifierName = clazz.getName();
        if (clazz.isAnnotationPresent(Component.class)) {
            String componentName = clazz.getAnnotation(Component.class).name();
            if (!componentName.isBlank()) qualifierName = clazz.getPackageName() + "." + componentName;
        }
        context.getScope().put(clazz, instance);
        context.getQualifierMap().put(qualifierName, instance);
    }

    public static <T> T getComponent(Class<T> clazz) {
        final List<Object> candidates = new ArrayList<>();
        for (Class<?> scopeClazz : context.getScope().keySet()) {
            if (clazz.isAssignableFrom(scopeClazz)){
                candidates.add(context.getScope().get(scopeClazz));
            }
        }

        if (candidates.isEmpty()) {
            logger.error("No injection candidate found for class {}", clazz);
            throw new ComponentCreationException("No injection candidate found for class " + clazz);
        } else if (candidates.size() > 1) {
            return getPrimaryComponent(candidates, clazz);
        }
        return clazz.cast(candidates.get(0));
    }

    public static <T> T getComponent(Class<T> clazz, String name) {
        final List<Object> candidates = new ArrayList<>();
        for (String componentName : context.getQualifierMap().keySet()) {
            if (componentName.substring(componentName.lastIndexOf('.') + 1).equalsIgnoreCase(name)) {
                candidates.add(context.getQualifierMap().get(componentName));
            }
        }

        if (candidates.isEmpty()) {
            logger.error("No injection candidate found for class {} with name {}", clazz, name);
            throw new ComponentCreationException("No injection candidate found for class " + clazz
                    + " with name " + name);
        } else if (candidates.size() > 1) {
            return getPrimaryComponent(candidates, clazz);
        }
        return clazz.cast(candidates.get(0));
    }

    private static <T> T getPrimaryComponent(List<Object> candidates, Class<T> clazz) {
        final List<Object> primaryCandidates = new ArrayList<>();
        for (Object candidate : candidates) {
            if (candidate.getClass().isAnnotationPresent(Primary.class)) {
                logger.debug("Primary candidate {} was found for class {}", candidate.getClass(), clazz);
                primaryCandidates.add(candidate);
            }
        }

        if (primaryCandidates.isEmpty()) {
            logger.warn("Multiple injection candidates found for class {}", clazz);
            logger.error("No primary candidate was defined for multiple injection candidates for class {}", clazz);
            throw new ComponentCreationException("Multiple injection candidates found for class " + clazz);
        } else if (primaryCandidates.size() > 1) {
            logger.error("Multiple primary injection candidates found for class {}", clazz);
            throw new ComponentCreationException("Multiple primary injection candidates found for class " + clazz);
        }
        return clazz.cast(primaryCandidates.get(0));
    }

    public static void clearContext(){
        context.getScope().clear();
        context.getQualifierMap().clear();
    }

    public Map<Class<?>, Object> getScope() {
        return scope;
    }

    public Map<String, Object> getQualifierMap() {
        return qualifierMap;
    }
}
