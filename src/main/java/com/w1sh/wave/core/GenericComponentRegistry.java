package com.w1sh.wave.core;

import com.w1sh.wave.core.exception.ComponentCreationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class GenericComponentRegistry implements ComponentRegistry {

    private static final Logger logger = LoggerFactory.getLogger(GenericComponentRegistry.class);

    private final ComponentDefinitionResolver definitionResolver;
    private final ComponentDefinitionFactory definitionFactory;
    private final Map<Class<?>, Object> scope;
    private final Map<Class<?>, AbstractComponentDefinition<?>> clazzDefinition;
    private final Map<String, Object> namedComponents;

    public GenericComponentRegistry() {
        this.definitionResolver = new GenericComponentDefinitionResolver(this);
        this.definitionFactory = new GenericComponentDefinitionFactory();
        scope = new HashMap<>();
        clazzDefinition = new HashMap<>();
        namedComponents = new HashMap<>();
    }

    @Override
    public <T> T register(Class<T> clazz) {
        return register(definitionFactory.create(clazz));
    }

    @Override
    public <T> T register(Class<T> clazz, String name) {
        return register(definitionFactory.create(clazz, name));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T register(AbstractComponentDefinition<?> definition) {
        clazzDefinition.put(definition.getClazz(), definition);
        final Object instance = definitionResolver.resolve(definition);
        scope.put(definition.getClazz(), instance);
        if (!definition.getName().isBlank()) {
            namedComponents.put(definition.getName(), instance);
        }
        return (T) instance;
    }

    @Override
    public <T> T resolve(Class<T> clazz) {
        try {
            return getComponent(clazz);
        } catch (ComponentCreationException e) {
            logger.debug("");
            return register(clazz);
        }
    }

    @Override
    public <T> Lazy<T> resolveLazy(Class<T> clazz) {
        return new LazyBinding<>(clazz, this);
    }

    @Override
    public <T> T resolve(Class<T> clazz, String name) {
        final T component = getComponent(clazz, name);
        if (component == null) {
            return register(clazz, name);
        }
        return component;
    }

    @Override
    public <T> T getComponent(Class<T> clazz) {
        final Map<Class<?>, Object> candidates = new HashMap<>();
        for (Map.Entry<Class<?>, Object> scopeClazz : scope.entrySet()) {
            if (clazz.isAssignableFrom(scopeClazz.getKey())){
                candidates.put(scopeClazz.getKey(), scopeClazz.getValue());
            }
        }

        if (candidates.isEmpty()) {
            logger.error("No injection candidate found for class {}", clazz);
            throw new ComponentCreationException("No injection candidate found for class " + clazz);
        } else if (candidates.size() == 1) {
            final Object candidate = candidates.values().iterator().next();
            return clazz.cast(candidate);
        }
        return resolveCandidates(clazz, candidates);
    }

    @Override
    public <T> T getComponent(Class<T> clazz, String name) {
        final Object instance = namedComponents.get(name);
        return instance != null ? clazz.cast(instance) : null;
    }

    @Override
    public void clearComponentMetadata() {
        clazzDefinition.clear();
    }

    @Override
    public void clear() {
        clearComponentMetadata();
        scope.clear();
        namedComponents.clear();
    }

    private <T> T resolveCandidates(Class<T> clazz, Map<Class<?>, Object> candidates){
        final Map<Class<?>, Object> primaryCandidates = new HashMap<>();
        for (Map.Entry<Class<?>, Object> aClass : candidates.entrySet()) {
            AbstractComponentDefinition<?> definition = clazzDefinition.get(aClass.getKey());

            if (definition.isPrimary()) {
                primaryCandidates.put(aClass.getKey(), aClass.getValue());
            }
        }

        candidates.keySet().removeAll(primaryCandidates.keySet());

        if (primaryCandidates.isEmpty()) {
            logger.warn("Multiple injection candidates found for class {}", clazz);
            logger.error("No primary candidate was defined for multiple injection candidates for class {}", clazz);
            throw new ComponentCreationException("Multiple injection candidates found for class " + clazz);
        } else if (primaryCandidates.size() > 1) {
            logger.error("Multiple primary injection candidates found for class {}", clazz);
            throw new ComponentCreationException("Multiple primary injection candidates found for class " + clazz);
        }
        final Object candidate = primaryCandidates.values().iterator().next();
        return clazz.cast(candidate);
    }
}
