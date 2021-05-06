package com.w1sh.wave.core;

import com.w1sh.wave.core.annotation.Primary;
import com.w1sh.wave.core.exception.UnsatisfiedComponentException;
import com.w1sh.wave.util.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class AbstractApplicationContext implements Registry, Configurable {

    private static final Logger logger = LoggerFactory.getLogger(AbstractApplicationContext.class);

    private final Map<Class<?>, Object> instances;
    private final Map<String, Object> namedInstances;

    private AbstractApplicationEnvironment environment;

    public AbstractApplicationContext() {
        this.instances = new HashMap<>(255);
        this.namedInstances = new HashMap<>(255);
        this.environment = ApplicationEnvironment.builder().build();
    }

    protected abstract void initialize();

    @Override
    public void register(Class<?> clazz, Object instance) {
        if (instances.containsKey(clazz) && !environment.isOverridingEnabled()) {
            logger.info("Instance of class {} already exists in scope", clazz);
            return;
        }
        instances.put(clazz, instance);
    }

    @Override
    public void register(String name, Object instance) {
        if (namedInstances.containsKey(name) && !environment.isOverridingEnabled()) {
            logger.info("Named instance {} already exists in scope", name);
            return;
        }
        namedInstances.put(name, instance);
    }

    @Override
    public <T> T getComponent(Class<T> clazz) {
        if (instances.containsKey(clazz)) {
            return clazz.cast(instances.get(clazz));
        }

        final List<T> componentsOfType = getComponentsOfType(clazz);
        if (componentsOfType.isEmpty()) {
            logger.error("No injection candidate found for class {}", clazz);
            if (environment.isNullComponentsAllowed()) return null;
            throw new UnsatisfiedComponentException("No injection candidate found for class " + clazz);
        } else if (componentsOfType.size() == 1) {
            return clazz.cast(componentsOfType.get(0));
        }
        return resolveCandidates(clazz, componentsOfType);
    }

    @Override
    public Object getComponent(String name) {
        final Object component = namedInstances.getOrDefault(name, null);
        if (component == null && !environment.isNullComponentsAllowed()) {
            throw new UnsatisfiedComponentException("No injection candidate found with name " + name);
        }
        return component;
    }

    @Override
    public <T> T getComponent(String name, Class<T> clazz) {
        final Object instance = getComponent(name);
        return instance != null ? clazz.cast(instance) : null;
    }

    @Override
    public <T> List<T> getComponentsOfType(Class<T> clazz) {
        final List<T> candidates = new ArrayList<>();
        for (Map.Entry<Class<?>, Object> scopeClazz : instances.entrySet()) {
            if (clazz.isAssignableFrom(scopeClazz.getKey())) {
                candidates.add(clazz.cast(scopeClazz.getValue()));
            }
        }
        return candidates;
    }

    @Override
    public boolean containsComponent(Class<?> clazz) {
        return !getComponentsOfType(clazz).isEmpty();
    }

    @Override
    public boolean containsComponent(Class<?> clazz, boolean allowSearchSubclasses) {
        if (allowSearchSubclasses) {
            return containsComponent(clazz);
        }
        return instances.containsKey(clazz);
    }

    @Override
    public boolean containsComponent(String name) {
        return namedInstances.containsKey(name);
    }

    @Override
    public Class<?> getType(String name) {
        final Object component = getComponent(name);
        if (component != null) {
            return component.getClass();
        }
        return null;
    }

    @Override
    public boolean isTypeMatch(String name, Class<?> clazz) {
        final Class<?> type = getType(name);
        return type != null && type.isAssignableFrom(clazz);
    }

    @Override
    public void clear() {
        instances.clear();
        namedInstances.clear();
    }

    private <T> T resolveCandidates(Class<T> clazz, List<T> componentsOfType) {
        final List<T> primaryCandidates = componentsOfType.stream()
                .filter(component -> ReflectionUtils.isAnnotationPresent(component.getClass(), Primary.class))
                .collect(Collectors.toList());

        if (primaryCandidates.isEmpty()) {
            logger.warn("Multiple injection candidates found for class {}", clazz);
            logger.error("No primary candidate was defined for multiple injection candidates for class {}", clazz);
            throw new UnsatisfiedComponentException("Multiple injection candidates found for class " + clazz);
        } else if (primaryCandidates.size() > 1) {
            logger.error("Multiple primary injection candidates found for class {}", clazz);
            throw new UnsatisfiedComponentException("Multiple primary injection candidates found for class " + clazz);
        }
        return clazz.cast(primaryCandidates.get(0));
    }

    @Override
    public AbstractApplicationEnvironment getEnvironment() {
        return this.environment;
    }

    @Override
    public void setEnvironment(AbstractApplicationEnvironment environment) {
        this.environment = environment;
    }
}
