package com.w1sh.wave.core;

import com.w1sh.wave.core.annotation.Conditional;
import com.w1sh.wave.core.annotation.Primary;
import com.w1sh.wave.util.Annotations;
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
        definition.setPrimary(Annotations.isAnnotationPresent(method, Primary.class));
        definition.setConditional(Annotations.isAnnotationPresent(method, Conditional.class));
        definition.setInjectionPoint(ReflectionUtils.injectionPointFromExecutable(method));
        definition.setName(method.getName());
        return definition;
    }

}
