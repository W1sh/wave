package com.w1sh.wave.core;

import com.w1sh.wave.condition.FilteringConditionalProcessor;
import com.w1sh.wave.condition.SimpleFilteringConditionalProcessor;
import com.w1sh.wave.core.annotation.Configuration;
import com.w1sh.wave.core.annotation.Nullable;
import com.w1sh.wave.core.annotation.Provides;
import com.w1sh.wave.core.annotation.Qualifier;
import com.w1sh.wave.core.exception.UnsatisfiedComponentException;
import com.w1sh.wave.util.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private void register(Collection<Definition> definitions) {
        final Map<Class<?>, Definition> classDefinitions = new HashMap<>(255);
        definitions.forEach(definition -> classDefinitions.put(definition.getClazz(), definition));

        for (Definition definition : classDefinitions.values()) {
            if (definition.getInjectionPoint() instanceof MethodInjectionPoint) {
                final MethodInjectionPoint injectionPoint = ((MethodInjectionPoint) definition.getInjectionPoint());
                register(classDefinitions.get(injectionPoint.getMethod().getDeclaringClass()));
                injectionPoint.setInstanceConfigurationClass(getComponent(injectionPoint.getMethod().getDeclaringClass()));
            }

            for (Type parameterType : definition.getInjectionPoint().getParameterTypes()) {
                if (parameterType instanceof Class) {
                    if (Modifier.isAbstract(((Class<?>) parameterType).getModifiers())) {
                        final List<? extends Definition> definitionsOfType = getDefinitionsOfType(((Class<?>) parameterType), classDefinitions);
                        definitionsOfType.forEach(this::register);
                    } else {
                        register(classDefinitions.get((Class<?>) parameterType));
                    }
                }
            }
            register(definition);
        }
    }

    private void register(Definition definition) {
        final Object instance = createInstance(definition);
        register(definition.getClazz(), instance);
        if (!definition.getName().isBlank()) {
            register(definition.getName(), instance);
        }
    }

    @Override
    public void initialize() {
        prepareContext();

        final List<Definition> definitions = scanner.scan();
        // separate conditional definitions
        final List<Definition> conditionalDefinitions = definitions.stream()
                .filter(Definition::isConditional)
                .collect(Collectors.toList());

        definitions.removeAll(conditionalDefinitions);

        // register non conditional definitions
        register(definitions);

        // create current context and pass it onto conditional processor
        var contextMetadata = new ContextMetadata(this, conditionalDefinitions, getEnvironment());
        FilteringConditionalProcessor conditionalProcessor = new SimpleFilteringConditionalProcessor(null);

        // filter conditional definitions based on components already initialized
        final List<Definition> passedConditionalDefinitions = conditionalProcessor.processConditionals(contextMetadata);

        // register conditional definitions
        register(passedConditionalDefinitions);
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
