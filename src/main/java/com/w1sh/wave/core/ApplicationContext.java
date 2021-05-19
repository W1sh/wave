package com.w1sh.wave.core;

import com.w1sh.wave.condition.ConditionalOnComponent;
import com.w1sh.wave.condition.ConditionalOnMissingComponent;
import com.w1sh.wave.condition.SimpleFilteringConditionalProcessor;
import com.w1sh.wave.core.annotation.*;
import com.w1sh.wave.core.exception.UnsatisfiedComponentException;
import com.w1sh.wave.util.Annotations;
import com.w1sh.wave.util.ReflectionUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class ApplicationContext extends AbstractApplicationContext {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationContext.class);

    private final ComponentScanner scanner;

    public ApplicationContext(ComponentScanner scanner) {
        super();
        this.scanner = scanner;
    }

    public ApplicationContext(ComponentScanner scanner, AbstractApplicationEnvironment environment) {
        this(scanner);
        setEnvironment(environment);
    }

    public static ApplicationContextBuilder builder() {
        return new ApplicationContextBuilder();
    }

    /**
     * Register an instance for all the {@link Definition} provided within this context.
     *
     * @param definitions The {@code definitions} to register.
     */
    private void register(Collection<Definition> definitions) {
        final Map<Class<?>, Definition> classDefinitions = definitions.stream()
                .collect(Collectors.toMap(Definition::getClazz, definition -> definition));

        for (Definition definition : classDefinitions.values()) {
            register(definition, classDefinitions);
        }
    }

    /**
     * Registers an instance of the {@link Definition} provided within this context.
     * <br>
     * If the {@code Definition} has an {@link InjectionPoint} with parameters, then those will be resolved
     * before this.
     * <br>
     * If the {@code Definition} has a {@link MethodInjectionPoint}, then the declaring class will be resolved
     * before this.
     *
     * @param definition  The {@code Definition} to create an instance for.
     * @param definitions The {@code Map<Class<?>, Definition>} containing all the classes with a {@code Definition}.
     */
    private void register(Definition definition, Map<Class<?>, Definition> definitions) {
        if (definition.isResolved()) {
            logger.debug("Definition for class {} has already been resolved.", definition.getClazz());
            return;
        }

        if (definition.isConditional() && !canConditionalBeInitialized(definition, definitions)) {
            logger.debug("Conditional for class {} evaluated to false. No instance will be created.", definition.getClazz());
            return;
        }

        if (definition.getInjectionPoint() instanceof MethodInjectionPoint) {
            logger.debug("Definition has method injection point, registering declaring class.");
            registerDeclaringClass(definition, definitions);
        }

        registerParameters(definition, definitions);

        final var instance = createInstance(definition);
        register(definition.getClazz(), instance);
        if (!definition.getName().isBlank()) {
            register(definition.getName(), instance);
        }
        definition.setResolved();
    }

    /**
     * Registers the components that the provided {@link Definition} depends on to be initialized. If the type of the
     * parameter is abstract, it will register all subtypes found in the {@code Map<Class<?>, Definition>} provided.
     *
     * @param definition  The {@code Definition} to create an instance for.
     * @param definitions The {@code Map<Class<?>, Definition>} containing all the classes with a {@code Definition}.
     */
    private void registerParameters(Definition definition, Map<Class<?>, Definition> definitions) {
        for (Type parameterType : definition.getInjectionPoint().getParameterTypes()) {
            if (parameterType instanceof Class) {
                final Class<?> type = (Class<?>) parameterType;
                if (definitions.containsKey(parameterType)) {
                    register(definitions.get(type), definitions);
                } else if (Modifier.isAbstract(type.getModifiers())) {
                    final var definitionsOfType = getDefinitionsOfType(type, definitions);
                    definitionsOfType.forEach(d -> register(d, definitions));
                } else {
                    // throw, unable to resolve parameter
                }
            }
        }
    }

    /**
     * Retrieves the declaring class of a {@link MethodInjectionPoint} and calls to register it within this context.
     *
     * @param definition  The {@code Definition} to create an instance for.
     * @param definitions The {@code Map<Class<?>, Definition>} containing all the classes with a {@code Definition}.
     */
    private void registerDeclaringClass(Definition definition, Map<Class<?>, Definition> definitions) {
        final var injectionPoint = ((MethodInjectionPoint) definition.getInjectionPoint());
        final var declaringClass = injectionPoint.getMethod().getDeclaringClass();
        if (definitions.containsKey(declaringClass)) {
            register(definitions.get(declaringClass), definitions);
            injectionPoint.setInstanceConfigurationClass(getComponent(declaringClass));
        } else {
            // throw, method injection point in non-configuration class
        }
    }

    private boolean canConditionalBeInitialized(Definition definition, Map<Class<?>, Definition> definitions) {
        for (Annotation conditional : Annotations.getAnnotationsOfType(definition.getClazz(), Conditional.class)) {
            if (conditional instanceof ConditionalOnComponent) {
                for (Class<?> aClass : ((ConditionalOnComponent) conditional).value()) {
                    final Definition classDef = Optional.ofNullable(definitions.getOrDefault(aClass, null))
                            .orElseThrow(() -> new UnsatisfiedComponentException("Conditional class depends on non-component class " + aClass));
                    register(classDef, definitions);
                }
            }
            if (conditional instanceof ConditionalOnMissingComponent) {
                for (Class<?> aClass : ((ConditionalOnMissingComponent) conditional).value()) {
                    final Definition classDef = Optional.ofNullable(definitions.getOrDefault(aClass, null))
                            .orElseThrow(() -> new UnsatisfiedComponentException("Conditional class depends on non-component class " + aClass));
                    register(classDef, definitions);
                }
            }
        }

        final var conditionalProcessor = new SimpleFilteringConditionalProcessor(new Reflections(""));
        return conditionalProcessor.evaluate(this, definition);
    }

    @Override
    public void initialize() {
        prepareContext();

        final List<Definition> definitions = scanner.scan();

        register(definitions);
    }

    private void prepareContext() {
        register(ApplicationContext.class, this);

        this.scanner.ignoreType(Configuration.class);
        this.scanner.ignoreType(Provides.class);
    }

    @Override
    public void setEnvironment(AbstractApplicationEnvironment environment) {
        super.setEnvironment(environment);
        this.scanner.setEnvironment(environment);
    }

    private List<Definition> getDefinitionsOfType(Class<?> clazz, Map<Class<?>, Definition> definitionMap) {
        final List<Definition> candidates = new ArrayList<>();
        for (Map.Entry<Class<?>, Definition> scopeClazz : definitionMap.entrySet()) {
            if (clazz.isAssignableFrom(scopeClazz.getKey())) {
                candidates.add(scopeClazz.getValue());
            }
        }
        return candidates;
    }

    private Object createInstance(Definition definition) {
        if (definition.getInjectionPoint().getParameterTypes() == null) {
            return ReflectionUtils.newInstance(definition.getInjectionPoint(), new Object[]{});
        }

        final Object[] params = new Object[definition.getInjectionPoint().getParameterTypes().length];
        for (int i = 0; i < definition.getInjectionPoint().getParameterTypes().length; i++) {
            final Type paramType = definition.getInjectionPoint().getParameterTypes()[i];
            final Qualifier qualifier = definition.getInjectionPoint().getQualifiers()[i];
            final Nullable nullable = definition.getInjectionPoint().getNullables()[i];

            if (paramType instanceof ParameterizedType) {
                params[i] = handleParameterizedType((ParameterizedType) paramType, qualifier);
            } else {
                params[i] = getComponent((Class<?>) paramType, qualifier, nullable);
            }
        }
        return ReflectionUtils.newInstance(definition.getInjectionPoint(), params);
    }

    private Object handleParameterizedType(ParameterizedType type, Qualifier qualifier) {
        if (type.getRawType().equals(Lazy.class)) {
            if (qualifier != null) {
                return new LazyBinding<>((Class<?>) type.getActualTypeArguments()[0], qualifier.name(), this);
            } else {
                return new LazyBinding<>((Class<?>) type.getActualTypeArguments()[0], this);
            }
        }

        if (type.getRawType().equals(Provider.class)) {
            if (qualifier != null) {
                return new ProviderBinding<>((Class<?>) type.getActualTypeArguments()[0], qualifier.name(), this);
            } else {
                return new ProviderBinding<>((Class<?>) type.getActualTypeArguments()[0], this);
            }
        }

        return getComponent((Class<?>) type.getRawType(), qualifier, null);
    }

    private Object getComponent(Class<?> clazz, Qualifier qualifier, Nullable nullable) {
        try {
            if (qualifier != null) {
                return getComponent(qualifier.name(), clazz);
            } else {
                return getComponent(clazz);
            }
        } catch (UnsatisfiedComponentException e) {
            if (nullable != null) {
                return null;
            }
            throw e;
        }
    }

    public ComponentScanner getScanner() {
        return scanner;
    }
}
