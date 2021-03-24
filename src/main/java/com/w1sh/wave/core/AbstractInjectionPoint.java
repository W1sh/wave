package com.w1sh.wave.core;

import com.w1sh.wave.core.annotation.Qualifier;

import java.lang.reflect.Constructor;

public abstract class AbstractInjectionPoint<T> {

    private final Constructor<T> constructor;
    private Qualifier[] qualifiers;
    private Class<?>[] parameterTypes;

    protected AbstractInjectionPoint(Constructor<T> constructor) {
        this.constructor = constructor;
    }

    public Constructor<T> getConstructor() {
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
