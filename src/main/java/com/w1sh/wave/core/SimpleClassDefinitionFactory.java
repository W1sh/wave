package com.w1sh.wave.core;

import com.w1sh.wave.core.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Type;

public class SimpleClassDefinitionFactory implements ClassDefinitionFactory {

    private static final Logger logger = LoggerFactory.getLogger(SimpleClassDefinitionFactory.class);

    @Override
    public Definition create(Class<?> clazz) {
        return toComponentDefinition(clazz);
    }

    private Definition toComponentDefinition(Class<?> aClass) {
        logger.debug("Creating component definition from class {}.", aClass);
        final Definition definition = new ComponentDefinition(aClass);
        final Component componentAnnotation = aClass.getAnnotation(Component.class);

        if (aClass.isAnnotationPresent(Primary.class)) {
            definition.setPrimary(true);
        }

        final Constructor<?> constructor = findAnnotatedConstructor(aClass);
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
        final InjectionPoint injectionPoint = new ConstructorInjectionPoint(constructor);
        if (constructor.getParameterCount() == 0) {
            return injectionPoint;
        }

        final Type[] parameterTypes = constructor.getGenericParameterTypes();
        final Qualifier[] qualifiers = new Qualifier[constructor.getParameterCount()];
        final Nullable[] nullables = new Nullable[constructor.getParameterCount()];

        for (int i = 0; i < parameterTypes.length; i++) {
            for (Annotation annotation : constructor.getParameterAnnotations()[i]) {
                if (annotation instanceof Qualifier) {
                    qualifiers[i] = (Qualifier) annotation;
                } else if (annotation instanceof Nullable) {
                    nullables[i] = (Nullable) annotation;
                }
            }
        }
        injectionPoint.setParameterTypes(parameterTypes);
        injectionPoint.setQualifiers(qualifiers);
        injectionPoint.setNullables(nullables);
        return injectionPoint;
    }

    private String createComponentName(Class<?> aClass, String name) {
        return (name != null && !name.isBlank()) ? aClass.getPackageName() + "." + name : "";
    }
}
