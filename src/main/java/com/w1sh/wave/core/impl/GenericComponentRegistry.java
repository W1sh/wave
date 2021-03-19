package com.w1sh.wave.core.impl;

import com.w1sh.wave.core.ComponentRegistry;

public class GenericComponentRegistry implements ComponentRegistry {

    @Override
    public <T> void register(Class<T> clazz, Object instance) {

    }

    @Override
    public <T> T getComponent(Class<T> clazz) {
        return null;
    }

    @Override
    public <T> T getComponent(Class<T> clazz, String name) {
        return null;
    }
}
