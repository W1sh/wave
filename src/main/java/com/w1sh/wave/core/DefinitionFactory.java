package com.w1sh.wave.core;

public interface DefinitionFactory<R> {

    Definition create(R clazz);

}
