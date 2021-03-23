package com.w1sh.wave.core;

public interface ComponentDefinitionFactory {

    AbstractComponentDefinition create(Class<?> clazz);

    AbstractComponentDefinition create(Class<?> clazz, String name);
}
