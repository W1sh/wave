package com.w1sh.wave.core;

public interface ComponentRegistry {

    <T> void register(Class<T> clazz, Object instance);

    <T> T getComponent(Class<T> clazz);

    <T> T getComponent(Class<T> clazz, String name);
}
