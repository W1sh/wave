package com.w1sh.wave.core;

import java.util.List;

public interface ComponentRegistry {

    <T> T register(Class<T> clazz);

    <T> T getComponent(Class<T> clazz);

    Object getComponent(String name);

    <T> T getComponent(String name, Class<T> clazz);

    <T> List<T> getComponentsOfType(Class<T> clazz);

    boolean containsComponentOfType(Class<?> clazz);

    void clear();
}
