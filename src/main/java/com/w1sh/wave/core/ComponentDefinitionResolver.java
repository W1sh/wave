package com.w1sh.wave.core;

public interface ComponentDefinitionResolver {

    <T> T resolve(ComponentDefinition componentDefinition);
}
