package com.w1sh.wave.core;

public interface ComponentRegistry {

    <T> T register(Class<T> clazz);

    <T> T register(Class<T> clazz, String name);

    <T> T register(AbstractComponentDefinition<?> componentDefinition);

    <T> T resolve(Class<T> clazz);

    <T> T resolve(Class<T> clazz, String name);

    <T> T getComponent(Class<T> clazz);

    <T> T getComponent(Class<T> clazz, String name);

    void clearComponentMetadata();

    void clear();
}
