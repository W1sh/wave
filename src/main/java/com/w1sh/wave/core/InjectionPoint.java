package com.w1sh.wave.core;

import java.lang.reflect.Type;

public abstract class InjectionPoint {

    private Type[] parameterTypes;
    private AnnotationMetadata[] parameterAnnotationMetadata;

    public Type[] getParameterTypes() {
        return parameterTypes != null ? parameterTypes : new Type[0];
    }

    public void setParameterTypes(Type[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public AnnotationMetadata[] getParameterAnnotationMetadata() {
        return parameterAnnotationMetadata;
    }

    public void setParameterAnnotationMetadata(AnnotationMetadata[] parameterAnnotationMetadata) {
        this.parameterAnnotationMetadata = parameterAnnotationMetadata;
    }
}
