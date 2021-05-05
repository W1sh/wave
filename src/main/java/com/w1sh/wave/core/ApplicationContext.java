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

public class ApplicationContext implements ComponentRegistry {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationContext.class);

    private final ComponentScanner scanner;
    private final Map<Class<?>, Object> instances;
    private final Map<Class<?>, Definition> classDefinitions;
    private final Map<String, Object> namedInstances;

    private AbstractApplicationEnvironment environment;

    public ApplicationContext(ComponentScanner scanner) {
        this.instances = new HashMap<>(255);
        this.classDefinitions = new HashMap<>(255);
        this.namedInstances = new HashMap<>(255);
        this.scanner = scanner;
        setEnvironment(ApplicationEnvironment.builder().build());
    }

    public ApplicationContext(ComponentScanner scanner, AbstractApplicationEnvironment environment) {
        this(scanner);
        setEnvironment(environment);
    }

    public static ApplicationContextBuilder builder(){
        return new ApplicationContextBuilder();
    }

    @Override
    public <T> void register(Class<T> clazz, T instance) {
        if (instances.containsKey(clazz) && !environment.isOverridingEnabled()) {
            logger.info("Instance of class {} already exists in scope", clazz);
            return;
        }
        instances.put(clazz, instance);
    }

    private void register(Collection<Definition> definitions) {
        definitions.forEach(definition -> classDefinitions.put(definition.getClazz(), definition));

        for (Definition definition : classDefinitions.values()) {
            if (definition.getInjectionPoint() instanceof MethodInjectionPoint) {
                final MethodInjectionPoint injectionPoint = ((MethodInjectionPoint) definition.getInjectionPoint());
                register(classDefinitions.get(injectionPoint.getMethod().getDeclaringClass()));
                injectionPoint.setInstanceConfigurationClass(instances.get(injectionPoint.getMethod().getDeclaringClass()));
            }

            for (Type parameterType : definition.getInjectionPoint().getParameterTypes()) {
                if (parameterType instanceof Class) {
                    if (Modifier.isAbstract(((Class<?>) parameterType).getModifiers())) {
                        final List<? extends Definition> definitionsOfType = getDefinitionsOfType(((Class<?>) parameterType));
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
        if (instances.containsKey(definition.getClazz()) && !environment.isOverridingEnabled()) {
            logger.info("Instance of class {} already exists in scope", definition.getClazz());
            return;
        }
        final Object instance = createInstance(definition);
        instances.put(definition.getClazz(), instance);
        if (!definition.getName().isBlank()) {
            namedInstances.put(definition.getName(), instance);
        }
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
            if (clazz.isAssignableFrom(scopeClazz.getKey())){
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

    @Override
    public void clear() {
        instances.clear();
        classDefinitions.clear();
        namedInstances.clear();
    }

    private void prepareContext() {
        register(ApplicationContext.class, this);

        this.scanner.ignoreType(Configuration.class);
        this.scanner.ignoreType(Provides.class);
    }

    @Override
    public AbstractApplicationEnvironment getEnvironment() {
        return this.environment;
    }

    @Override
    public void setEnvironment(AbstractApplicationEnvironment environment) {
        this.environment = environment;
        this.scanner.setEnvironment(environment);
    }

    private <T> T resolveCandidates(Class<T> clazz, List<T> componentsOfType){
        final List<T> primaryCandidates = componentsOfType.stream()
                .filter(component -> classDefinitions.get(component.getClass()).isPrimary())
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

    private List<Definition> getDefinitionsOfType(Class<?> clazz) {
        final List<Definition> candidates = new ArrayList<>();
        for (Map.Entry<Class<?>, Definition> scopeClazz : classDefinitions.entrySet()) {
            if (clazz.isAssignableFrom(scopeClazz.getKey())){
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
