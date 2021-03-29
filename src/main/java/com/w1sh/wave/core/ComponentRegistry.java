package com.w1sh.wave.core;

import java.util.Collection;
import java.util.List;

public interface ComponentRegistry {

    void register(Collection<Class<?>> clazz);

    <T> T register(Class<T> clazz);

    <T> T register(String name, Class<T> clazz);

    <T> T register(AbstractComponentDefinition<?> componentDefinition);

    void registerDefinitions(Collection<AbstractComponentDefinition<?>> componentDefinition);

    <T> T getComponent(Class<T> clazz);

    Object getComponent(String name);

    <T> T getComponent(String name, Class<T> clazz);

    <T> List<T> getComponentsOfType(Class<T> clazz);

    void clear();
}
