package com.w1sh.wave.core;

import java.util.List;

public interface ComponentRegistry extends Configurable {

    void initialize(List<Definition> definitions);

    <T> void register(Class<T> clazz, T instance);

    <T> T getComponent(Class<T> clazz);

    Object getComponent(String name);

    <T> T getComponent(String name, Class<T> clazz);

    <T> List<T> getComponentsOfType(Class<T> clazz);

    boolean containsComponentOfType(Class<?> clazz);

    /**
     * Clears the registry of all the components and metadata
     */
    void clear();
}
