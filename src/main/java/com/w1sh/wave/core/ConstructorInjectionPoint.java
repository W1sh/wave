package com.w1sh.wave.core;

import java.lang.reflect.Constructor;

public class ConstructorInjectionPoint extends InjectionPoint {

    private final Constructor<?> constructor;

    public ConstructorInjectionPoint(Constructor<?> constructor) {
        super();
        this.constructor = constructor;
    }

    public Constructor<?> getConstructor() {
        return constructor;
    }
}
