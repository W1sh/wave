package com.w1sh.wave.core;

import com.w1sh.wave.core.annotation.Conditional;
import com.w1sh.wave.core.annotation.Primary;
import com.w1sh.wave.core.annotation.Priority;
import com.w1sh.wave.util.Annotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class SimpleMethodDefinitionFactory implements MethodDefinitionFactory {

    private static final Logger logger = LoggerFactory.getLogger(SimpleMethodDefinitionFactory.class);
    private final InjectionPointFactory injectionPointFactory = new SimpleInjectionPointFactory();

    @Override
    public Definition create(Method method) {
        logger.debug("Creating component definition from method {}.", method.getName());
        final var definition = new ComponentDefinition(method.getReturnType());
        definition.setPrimary(Annotations.isAnnotationPresent(method, Primary.class));
        definition.setConditional(Annotations.isAnnotationPresent(method, Conditional.class));
        definition.setPriority((Priority) Annotations.getAnnotationOfType(method, Priority.class).orElse(null));
        definition.setInjectionPoint(injectionPointFactory.create(method));
        definition.setName(method.getName());
        return definition;
    }
}
