package com.w1sh.wave.core;

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
    public Object resolve(AbstractComponentDefinition<?> definition) {
        if (definition.getInjectionPoint().getParameterTypes() == null) {
            return createInstance(definition.getClazz(), definition.getInjectionPoint().getConstructor());
        }

        final Object[] params = new Object[definition.getInjectionPoint().getParameterTypes().length];

        for (int i = 0; i < definition.getInjectionPoint().getParameterTypes().length; i++) {
            final Class<?> paramClass = definition.getInjectionPoint().getParameterTypes()[i];
            if (definition.getInjectionPoint().getQualifiers()[i] != null) {
                params[i] = resolveDependency(paramClass, definition.getInjectionPoint().getQualifiers()[i].name());
            } else {
                params[i] = resolveDependency(paramClass);
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

    private <T> T resolveDependency(Class<T> clazz, String name) {
        try {
            return registry.getComponent(name, clazz);
        } catch (ComponentCreationException e) {
            logger.warn("Required dependency is not yet present in the registry.");
            return registry.register(name, clazz);
        }
    }

    private <T> T resolveDependency(Class<T> clazz) {
        try {
            return registry.getComponent(clazz);
        } catch (ComponentCreationException e) {
            logger.warn("Required dependency is not yet present in the registry.");
            return registry.register(clazz);
        }
    }
}
