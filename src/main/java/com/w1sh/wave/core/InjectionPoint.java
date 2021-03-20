package com.w1sh.wave.core;

import com.w1sh.wave.core.annotation.Qualifier;

import java.lang.reflect.Constructor;

public class InjectionPoint {

    private final Constructor<?> constructor;
    private Qualifier[] qualifiers;
    private Class<?>[] parameterTypes;

    public InjectionPoint(Constructor<?> constructor) {
        this.constructor = constructor;
    }

    public Constructor<?> getConstructor() {
        return constructor;
    }

    public Qualifier[] getQualifiers() {
        return qualifiers;
    }

    public void setQualifiers(Qualifier[] qualifiers) {
        this.qualifiers = qualifiers;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }
}
