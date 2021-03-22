package com.w1sh.wave.core;

import java.util.Set;

public interface ComponentRegistry {

    void register();

    void register(AbstractComponentDefinition componentDefinition);

    void fillWithComponentMetadata(Set<AbstractComponentDefinition> definitions);

    <T> T getComponent(Class<T> clazz);

    <T> T getComponent(Class<T> clazz, String name);

    void clearComponentMetadata();

    void clear();
}
