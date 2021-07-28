package com.w1sh.wave.core;

import com.w1sh.wave.core.annotation.*;
import com.w1sh.wave.util.Annotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class SimpleClassDefinitionFactory implements ClassDefinitionFactory {

    private static final Logger logger = LoggerFactory.getLogger(SimpleClassDefinitionFactory.class);
    private final ComponentNameGenerator nameGenerator = new QualifiedComponentNameGenerator();
    private final InjectionPointFactory injectionPointFactory = new SimpleInjectionPointFactory();

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
        definition.setPriority((Priority) Annotations.getAnnotationOfType(aClass, Priority.class).orElse(null));
        definition.setInjectionPoint(injectionPointFactory.create(constructor));
        definition.setName(nameGenerator.generate(aClass, aClass.getAnnotation(Component.class)));

        retrievePostConstructorMethods(aClass, definition);
        return definition;
    }

    private void retrievePostConstructorMethods(Class<?> aClass, ComponentDefinition definition) {
        final List<Method> postConstructorMethods = new ArrayList<>();
        for (Method method : aClass.getMethods()) {
            if (Annotations.isAnnotationPresent(method, PostConstruct.class)) {
                postConstructorMethods.add(method);
            }
        }
        definition.setPostConstructorMethods(postConstructorMethods);
    }

    private Constructor<?> findAnnotatedConstructor(Class<?> aClass) {
        for (Constructor<?> constructor : aClass.getConstructors()) {
            if (constructor.isAnnotationPresent(Inject.class)) {
                return constructor;
            }
        }
        return aClass.getConstructors()[0];
    }
}
