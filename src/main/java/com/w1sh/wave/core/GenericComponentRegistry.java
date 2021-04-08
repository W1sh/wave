package com.w1sh.wave.core;

import com.w1sh.wave.core.exception.ComponentCreationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GenericComponentRegistry implements ComponentRegistry {

    private static final Logger logger = LoggerFactory.getLogger(GenericComponentRegistry.class);

    private final ComponentDefinitionFactory factory;
    private final ComponentDefinitionResolver definitionResolver;
    private final Map<Class<?>, Object> scope;
    private final Map<Class<?>, AbstractComponentDefinition<?>> definitions;
    private final Map<String, Object> namedComponents;

    public GenericComponentRegistry() {
        this.factory = new GenericComponentDefinitionFactory();
        this.definitionResolver = new GenericComponentDefinitionResolver(this);
        scope = new HashMap<>(255);
        definitions = new HashMap<>(255);
        namedComponents = new HashMap<>(255);
    }


    @Override
    public <T> T register(Class<T> clazz) {
        final AbstractComponentDefinition<T> definition = factory.create(clazz);
        return register(definition);
    }

    @SuppressWarnings("unchecked")
    private <T> T register(AbstractComponentDefinition<T> definition) {
        final Object instance = definitionResolver.resolve(definition);
        scope.put(definition.getClazz(), instance);
        if (!definition.getName().isBlank()) {
            namedComponents.put(definition.getName(), instance);
        }
        return (T) instance;
    }

    /**
     * Returns the a component of the given class. If no component is found, it will attempt to find one for a subclass
     * of the given class. If no candidates are found it will throw a {@link ComponentCreationException}.
     *
     * @param clazz The {@link Class} of the component
     * @param <T> The type of the component
     * @return The component, or null if none was found.
     * @throws ComponentCreationException if no component is found for the given class.
     */
    @Override
    public <T> T getComponent(Class<T> clazz) {
        if (scope.containsKey(clazz)) {
            return clazz.cast(scope.get(clazz));
        }

        final List<T> componentsOfType = getComponentsOfType(clazz);
        if (componentsOfType.isEmpty()) {
            logger.error("No injection candidate found for class {}", clazz);
            throw new ComponentCreationException("No injection candidate found for class " + clazz);
        } else if (componentsOfType.size() == 1) {
            return clazz.cast(componentsOfType.get(0));
        }
        return resolveCandidates(clazz, componentsOfType);

    }

    /**
     * Returns the component for the given name.
     *
     * @param name The name of the component.
     * @return The component of the given name, or null if none was found.
     */
    @Override
    public Object getComponent(String name) {
        return namedComponents.getOrDefault(name, null);
    }

    /**
     * Returns the component for the given name and casts it to the given class. Might throw {@link ClassCastException}
     * if the component is cast to a subclass of which it is not an instance
     *
     * @param name The name of the component.
     * @param clazz The {@link Class} of the component.
     * @param <T> The type of the component.
     * @return The component of the given name, or null if none was found.
     */
    @Override
    public <T> T getComponent(String name, Class<T> clazz) {
        final Object instance = getComponent(name);
        return instance != null ? clazz.cast(instance) : null;
    }

    @Override
    public <T> List<T> getComponentsOfType(Class<T> clazz) {
        final List<T> candidates = new ArrayList<>();
        for (Map.Entry<Class<?>, Object> scopeClazz : scope.entrySet()) {
            if (clazz.isAssignableFrom(scopeClazz.getKey())){
                candidates.add(clazz.cast(scopeClazz.getValue()));
            }
        }
        return candidates;
    }

    @Override
    public boolean containsComponentOfType(Class<?> clazz) {
        return !getComponentsOfType(clazz).isEmpty();
    }

    @Override
    public void clear() {
        scope.clear();
        definitions.clear();
        namedComponents.clear();
    }

    private <T> T resolveCandidates(Class<T> clazz, List<T> componentsOfType){
        final List<T> primaryCandidates = componentsOfType.stream()
                .filter(component -> definitions.get(component.getClass()).isPrimary())
                .collect(Collectors.toList());

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
}
