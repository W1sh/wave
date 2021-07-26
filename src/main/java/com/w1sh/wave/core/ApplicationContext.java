package com.w1sh.wave.core;

import com.w1sh.wave.condition.ConditionalOnComponent;
import com.w1sh.wave.condition.ConditionalOnMissingComponent;
import com.w1sh.wave.condition.SimpleFilteringConditionalProcessor;
import com.w1sh.wave.core.annotation.Conditional;
import com.w1sh.wave.core.annotation.Configuration;
import com.w1sh.wave.core.annotation.Provides;
import com.w1sh.wave.core.exception.UnsatisfiedComponentException;
import com.w1sh.wave.util.Annotations;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
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
     * Register an instance for all the {@link Definition} provided within this context. Will register definitions with
     * higher priority first.
     *
     * @param definitions The {@code definitions} to register.
     */
    private void register(Collection<Definition> definitions) {
        final Map<Class<?>, Definition> classDefinitions = definitions.stream()
                .collect(Collectors.toMap(Definition::getClazz, definition -> definition));

        definitions.stream()
                .sorted(Comparator.comparingInt(definition -> definition.getPriority().value()))
                .forEach(definition -> register(definition, classDefinitions));
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

        register(definition);

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

    public ComponentScanner getScanner() {
        return scanner;
    }
}
