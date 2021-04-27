package com.w1sh.wave.core;

public interface DefinitionFactory<R> {

    <T> Definition<T> create(R clazz);

}
