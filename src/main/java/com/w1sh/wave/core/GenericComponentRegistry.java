package com.w1sh.wave.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class GenericComponentRegistry implements ComponentRegistry {

    private static final Logger logger = LoggerFactory.getLogger(GenericComponentRegistry.class);

    private final Map<Class<?>, Object> scope;
    private final Map<String, Object> qualifierMap;

    public GenericComponentRegistry() {
        scope = new HashMap<>();
        qualifierMap = new HashMap<>();
    }

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
