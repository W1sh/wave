package com.w1sh.wave.core;

import java.lang.reflect.Method;

public class MethodInjectionPoint extends InjectionPoint {

    private final Object instanceConfigurationClass;
    private final Method method;

    protected MethodInjectionPoint(Object instanceConfigurationClass, Method method) {
        super();
        this.instanceConfigurationClass = instanceConfigurationClass;
        this.method = method;
    }

    public Object getInstanceConfigurationClass() {
        return instanceConfigurationClass;
    }

    public Method getMethod() {
        return method;
    }
}
