package com.w1sh.wave.core.impl;

import com.w1sh.wave.core.ComponentDefinition;
import com.w1sh.wave.core.ComponentScanner;
import com.w1sh.wave.core.annotation.Component;
import com.w1sh.wave.core.annotation.Primary;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        definition.setName(createComponentName(aClass, componentAnnotation.name()));
        return definition;
    }

    private String createComponentName(Class<?> aClass, String name){
        if (name.isBlank()) {
            return aClass.getPackageName() + "." + name;
        }
        return aClass.getName();
    }
}
