package com.w1sh.wave.core;

import com.w1sh.wave.core.annotation.Component;
import com.w1sh.wave.core.annotation.Conditional;
import com.w1sh.wave.core.annotation.Inject;
import com.w1sh.wave.core.annotation.Primary;
import com.w1sh.wave.util.Annotations;
import com.w1sh.wave.util.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

public class SimpleClassDefinitionFactory implements ClassDefinitionFactory {

    private static final Logger logger = LoggerFactory.getLogger(SimpleClassDefinitionFactory.class);

    @Override
    public Definition create(Class<?> clazz) {
        return toComponentDefinition(clazz);
    }

    private Definition toComponentDefinition(Class<?> aClass) {
        if (Modifier.isAbstract(aClass.getModifiers())) {
            logger.warn("Can not convert abstract class {} into a definition.", aClass);
            return null;
        }

        logger.debug("Creating component definition from class {}.", aClass);
        final var definition = new ComponentDefinition(aClass);
        final var constructor = findAnnotatedConstructor(aClass);
        definition.setPrimary(Annotations.isAnnotationPresent(aClass, Primary.class));
        definition.setConditional(Annotations.isAnnotationPresent(aClass, Conditional.class));
        definition.setInjectionPoint(ReflectionUtils.injectionPointFromExecutable(constructor));
        definition.setName(createComponentName(aClass, aClass.getAnnotation(Component.class).name()));
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

    private String createComponentName(Class<?> aClass, String name) {
        return (name != null && !name.isBlank()) ? aClass.getPackageName() + "." + name : "";
    }

}
