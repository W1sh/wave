package com.w1sh.wave.core;

import com.w1sh.wave.core.annotation.Primary;
import com.w1sh.wave.util.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class SimpleMethodDefinitionFactory implements MethodDefinitionFactory {

    private static final Logger logger = LoggerFactory.getLogger(SimpleMethodDefinitionFactory.class);

    @Override
    public Definition create(Method method) {
        return toComponentDefinition(method);
    }

    private Definition toComponentDefinition(Method method) {
        logger.debug("Creating component definition from method {}.", method.getName());
        final var definition = new ComponentDefinition(method.getReturnType());
        definition.setPrimary(method.isAnnotationPresent(Primary.class));
        definition.setInjectionPoint(ReflectionUtils.injectionPointFromExecutable(method));
        definition.setName(method.getName());
        return definition;
    }

}
