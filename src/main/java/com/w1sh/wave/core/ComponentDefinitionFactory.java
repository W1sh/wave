package com.w1sh.wave.core;

public interface ComponentDefinitionFactory {

    <T> AbstractComponentDefinition<T> create(Class<T> clazz);

    <T> AbstractComponentDefinition<T> create(Class<T> clazz, String name);
}
