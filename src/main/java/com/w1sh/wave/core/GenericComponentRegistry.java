package com.w1sh.wave.core;

import com.w1sh.wave.core.exception.ComponentCreationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class GenericComponentRegistry implements ComponentRegistry {

    private static final Logger logger = LoggerFactory.getLogger(GenericComponentRegistry.class);

    private final ComponentDefinitionResolver definitionResolver;
    private final Map<Class<?>, Object> scope;
    private final Map<Class<?>, AbstractComponentDefinition> clazzDefinition;
    private final Map<String, Object> namedComponents;

    public GenericComponentRegistry() {
        this.definitionResolver = new GenericComponentDefinitionResolver(this);
        scope = new HashMap<>();
        clazzDefinition = new HashMap<>();
        namedComponents = new HashMap<>();
    }

    @Override
    public void register() {
        for (AbstractComponentDefinition definition : clazzDefinition.values()) {
            register(definition);
        }
    }

    @Override
    public void register(AbstractComponentDefinition definition) {
        final Object instance = definitionResolver.resolve(definition);
        scope.put(definition.getClazz(), instance);
        if (!definition.getName().isBlank()) {
            namedComponents.put(definition.getName(), instance);
        }
    }

    @Override
    public void fillWithComponentMetadata(Set<AbstractComponentDefinition> definitions) {
        for (AbstractComponentDefinition definition : definitions) {
            clazzDefinition.put(definition.getClazz(), definition);
        }
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
        scope.clear();
        clazzDefinition.clear();
        namedComponents.clear();
    }

    private <T> T resolveCandidates(Class<T> clazz, Map<Class<?>, Object> candidates){
        final Map<Class<?>, Object> primaryCandidates = new HashMap<>();
        for (Map.Entry<Class<?>, Object> aClass : candidates.entrySet()) {
            AbstractComponentDefinition definition = clazzDefinition.get(aClass.getKey());

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
