package com.w1sh.wave.core;

import java.util.Set;

public interface ComponentRegistry {

    void register(ComponentDefinition componentDefinition);

    void fillWithComponentMetadata(Set<ComponentDefinition> definitions);

    <T> T getComponent(Class<T> clazz);

    <T> T getComponent(Class<T> clazz, String name);

    void clearComponentMetadata();

    void clear();
}
