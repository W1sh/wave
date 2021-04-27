package com.w1sh.wave.core;

import com.w1sh.wave.core.annotation.Nullable;
import com.w1sh.wave.core.annotation.Qualifier;

import java.lang.reflect.Type;

public abstract class InjectionPoint {

    private Qualifier[] qualifiers;
    private Type[] parameterTypes;
    private Nullable[] nullables;

    public Qualifier[] getQualifiers() {
        return qualifiers != null ? qualifiers : new Qualifier[0];
    }

    public void setQualifiers(Qualifier[] qualifiers) {
        this.qualifiers = qualifiers;
    }

    public Type[] getParameterTypes() {
        return parameterTypes != null ? parameterTypes : new Type[0];
    }

    public void setParameterTypes(Type[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Nullable[] getNullables() {
        return nullables;
    }

    public void setNullables(Nullable[] nullables) {
        this.nullables = nullables;
    }
}
