package com.w1sh.wave.core;

import com.w1sh.wave.core.annotation.Qualifier;
import com.w1sh.wave.core.exception.ComponentCreationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GenericComponentRegistry implements ComponentRegistry {

    private static final Logger logger = LoggerFactory.getLogger(GenericComponentRegistry.class);

    private final ComponentDefinitionFactory factory;
    private final Map<Class<?>, Object> scope;
    private final Map<Class<?>, AbstractComponentDefinition<?>> definitions;
    private final Map<String, Object> namedComponents;

    private AbstractApplicationEnvironment environment;

    public GenericComponentRegistry() {
        this(null);
    }

    public GenericComponentRegistry(ComponentDefinitionFactory factory) {
        this.scope = new HashMap<>(255);
        this.definitions = new HashMap<>(255);
        this.namedComponents = new HashMap<>(255);
        this.factory = factory != null ? factory : new GenericComponentDefinitionFactory();
    }

    @Override
    public void registerMetadata(List<Class<?>> classes) {
        classes.forEach(c -> definitions.put(c, factory.create(c)));
    }

    @Override
    public void register(Class<?> clazz) {
        if (scope.containsKey(clazz) && !environment.isOverridingEnabled()) {
            logger.info("Instance of class {} already exists in scope", clazz);
            return;
        }

        if (Modifier.isAbstract(clazz.getModifiers())) {
            final List<? extends AbstractComponentDefinition<?>> definitionsOfType = getDefinitionsOfType(clazz);
            definitionsOfType.forEach(this::register);
            return;
        }

        final AbstractComponentDefinition<?> definition = definitions.get(clazz);
        for (Type parameterType : definition.getInjectionPoint().getParameterTypes()) {
            if (parameterType instanceof Class) {
                if (Modifier.isAbstract(((Class<?>) parameterType).getModifiers())) {
                    final List<? extends AbstractComponentDefinition<?>> definitionsOfType = getDefinitionsOfType(((Class<?>) parameterType));
                    definitionsOfType.forEach(this::register);
                } else {
                    register((Class<?>) parameterType);
                }
            }
        }
        register(definition);
    }

    private void register(AbstractComponentDefinition<?> definition) {
        final Object instance = createInstance(definition);
        scope.put(definition.getClazz(), instance);
        if (!definition.getName().isBlank()) {
            namedComponents.put(definition.getName(), instance);
        }
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

    /**
     * Returns all the components that match the given class.
     *
     * @param clazz The {@link Class} of the component.
     * @param <T> The type of the component.
     * @return A {@link List} containing all the components that matched.
     */
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

    /**
     * Checks if one or multiple components match for the given {@link Class}.
     *
     * @param clazz The {@link Class} of the component.
     * @return True if there are components for the given class, false otherwise.
     */
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

    @SuppressWarnings("unchecked")
    private <T> List<AbstractComponentDefinition<T>> getDefinitionsOfType(Class<T> clazz) {
        final List<AbstractComponentDefinition<T>> candidates = new ArrayList<>();
        for (Map.Entry<Class<?>, AbstractComponentDefinition<?>> scopeClazz : definitions.entrySet()) {
            if (clazz.isAssignableFrom(scopeClazz.getKey())){
                candidates.add((AbstractComponentDefinition<T>) scopeClazz.getValue());
            }
        }
        return candidates;
    }

    private Object createInstance(AbstractComponentDefinition<?> definition) {
        if (definition.getInjectionPoint().getParameterTypes() == null) {
            return definition.getInjectionPoint().create(new Object[]{});
        }

        final Object[] params = new Object[definition.getInjectionPoint().getParameterTypes().length];
        for (int i = 0; i < definition.getInjectionPoint().getParameterTypes().length; i++) {
            final Type paramType = definition.getInjectionPoint().getParameterTypes()[i];
            final Qualifier qualifier = definition.getInjectionPoint().getQualifiers()[i];

            if (paramType instanceof ParameterizedType) {
                params[i] = handleParameterizedType((ParameterizedType) paramType, qualifier);
                break;
            }

            if (qualifier != null) {
                params[i] = getComponent(qualifier.name(), (Class<?>) paramType);
            } else {
                params[i] = getComponent((Class<?>) paramType);
            }
        }
        return definition.getInjectionPoint().create(params);
    }

    private Object handleParameterizedType(ParameterizedType type, Qualifier qualifier) {
        if (type.getRawType().equals(Lazy.class)) {
            if (qualifier != null) {
                return new LazyBinding<>((Class<?>) type.getActualTypeArguments()[0], qualifier.name(), this);
            } else {
                return new LazyBinding<>((Class<?>) type.getActualTypeArguments()[0], this);
            }
        }

        return getComponent((Class<?>) type.getRawType());
    }

    @Override
    public AbstractApplicationEnvironment getEnvironment() {
        return environment;
    }

    @Override
    public void setEnvironment(AbstractApplicationEnvironment environment) {
        this.environment = environment;
    }
}
