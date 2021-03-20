package com.w1sh.wave.core.impl;

import com.w1sh.wave.core.ComponentDefinition;
import com.w1sh.wave.core.ComponentDefinitionResolver;
import com.w1sh.wave.core.ComponentRegistry;
import com.w1sh.wave.core.exception.ComponentCreationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class GenericComponentDefinitionResolver implements ComponentDefinitionResolver {

    private static final Logger logger = LoggerFactory.getLogger(GenericComponentDefinitionResolver.class);

    private final ComponentRegistry registry;

    public GenericComponentDefinitionResolver(ComponentRegistry registry) {
        this.registry = registry;
    }

    @Override
    public Object resolve(ComponentDefinition definition) {
        final Object[] params = new Object[definition.getInjectionPoint().getParameterTypes().length];

        for (int i = 0; i < definition.getInjectionPoint().getParameterTypes().length; i++) {
            final Class<?> paramClass = definition.getInjectionPoint().getParameterTypes()[i];
            final String name = definition.getInjectionPoint().getQualifiers()[i].name();
            if (!name.isBlank()) {
                params[i] = registry.getComponent(paramClass, name);
            } else {
                params[i] = registry.getComponent(paramClass);
            }
        }

        return createInstance(definition.getClazz(), definition.getInjectionPoint().getConstructor(), params);
    }

    private Object createInstance(Class<?> classToInject, Constructor<?> constructor, Object... params) {
        logger.debug("Creating new instance of class {}", classToInject.getSimpleName());
        try {
            return constructor.newInstance(params);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new ComponentCreationException("Unable to create an instance of the class " + classToInject, e);
        }
    }
}
