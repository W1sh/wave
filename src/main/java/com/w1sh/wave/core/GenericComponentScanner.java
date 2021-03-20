package com.w1sh.wave.core;

import com.w1sh.wave.core.annotation.Component;
import com.w1sh.wave.core.annotation.Inject;
import com.w1sh.wave.core.annotation.Primary;
import com.w1sh.wave.core.annotation.Qualifier;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Set;

public class GenericComponentScanner implements ComponentScanner {

    private static final Logger logger = LoggerFactory.getLogger(GenericComponentScanner.class);

    private final Reflections reflections;

    public GenericComponentScanner(String packagePrefix) {
        this.reflections = new Reflections(packagePrefix);
    }

    @Override
    public Set<ComponentDefinition> scan() {
        final Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Component.class);
        final Set<ComponentDefinition> componentDefinitions = new HashSet<>();
        logger.debug("Found {} classes annotated to be initialized.", classes.size());

        for (Class<?> aClass : classes) {
            componentDefinitions.add(toComponentDefinition(aClass));
        }
        return componentDefinitions;
    }

    private ComponentDefinition toComponentDefinition(Class<?> aClass) {
        logger.debug("Creating component definition from class {}.", aClass);
        final ComponentDefinition definition = new ComponentDefinition(aClass);
        final Component componentAnnotation = aClass.getAnnotation(Component.class);

        if (aClass.isAnnotationPresent(Primary.class)) {
            definition.setPrimary(true);
        }
        if (componentAnnotation.lazy()) {
            definition.setLazy(true);
        }

        Constructor<?> constructor = findAnnotatedConstructor(aClass);
        definition.setInjectionPoint(toInjectionPoint(constructor));
        definition.setName(createComponentName(aClass, componentAnnotation.name()));
        return definition;
    }

    private Constructor<?> findAnnotatedConstructor(Class<?> aClass) {
        for (Constructor<?> constructor : aClass.getConstructors()) {
            if (constructor.isAnnotationPresent(Inject.class)) {
                return constructor;
            }
        }
        return aClass.getConstructors()[0];
    }

    private InjectionPoint toInjectionPoint(Constructor<?> constructor) {
        final InjectionPoint injectionPoint = new InjectionPoint(constructor);
        if (constructor.getParameterCount() == 0) {
            return injectionPoint;
        }

        final Class<?>[] parameterTypes = constructor.getParameterTypes();
        final Qualifier[] qualifiers = new Qualifier[constructor.getParameterCount()];

        for (int i = 0; i < parameterTypes.length; i++) {
            for (Annotation annotation : constructor.getParameterAnnotations()[i]) {
                if (annotation instanceof Qualifier) {
                    qualifiers[i] = (Qualifier) annotation;
                    break;
                }
            }
        }
        injectionPoint.setParameterTypes(parameterTypes);
        injectionPoint.setQualifiers(qualifiers);
        return injectionPoint;
    }

    private String createComponentName(Class<?> aClass, String name) {
        if (name.isBlank()) {
            return aClass.getPackageName() + "." + name;
        }
        return aClass.getName();
    }
}
