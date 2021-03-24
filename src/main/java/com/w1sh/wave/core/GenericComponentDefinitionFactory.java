package com.w1sh.wave.core;

import com.w1sh.wave.core.annotation.Component;
import com.w1sh.wave.core.annotation.Inject;
import com.w1sh.wave.core.annotation.Primary;
import com.w1sh.wave.core.annotation.Qualifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

public class GenericComponentDefinitionFactory implements ComponentDefinitionFactory {

    private static final Logger logger = LoggerFactory.getLogger(GenericComponentDefinitionFactory.class);

    @Override
    public AbstractComponentDefinition create(Class<?> clazz) {
        return toComponentDefinition(clazz, null);
    }

    @Override
    public AbstractComponentDefinition create(Class<?> clazz, String name) {
        return toComponentDefinition(clazz, name);
    }

    private AbstractComponentDefinition toComponentDefinition(Class<?> aClass, String name) {
        logger.debug("Creating component definition from class {}.", aClass);
        final AbstractComponentDefinition definition = new ComponentDefinition(aClass);
        final Component componentAnnotation = aClass.getAnnotation(Component.class);

        if (aClass.isAnnotationPresent(Primary.class)) {
            definition.setPrimary(true);
        }
        if (componentAnnotation.lazy()) {
            definition.setLazy(true);
        }

        final Constructor<?> constructor = findAnnotatedConstructor(aClass);
        definition.setInjectionPoint(toInjectionPoint(constructor));
        final String componentName = name != null ? name : componentAnnotation.name();
        definition.setName(createComponentName(aClass, componentName));
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

    private <T> AbstractInjectionPoint<T> toInjectionPoint(Constructor<T> constructor) {
        final AbstractInjectionPoint<T> injectionPoint = new InjectionPoint<>(constructor);
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
        if (!name.isBlank()) {
            return aClass.getPackageName() + "." + name;
        }
        return "";
    }
}