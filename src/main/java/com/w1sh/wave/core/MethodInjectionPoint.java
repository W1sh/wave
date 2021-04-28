package com.w1sh.wave.core;

import java.lang.reflect.Method;

public class MethodInjectionPoint extends InjectionPoint {

    private final Method method;
    private Object instanceConfigurationClass;

    public MethodInjectionPoint(Method method) {
        super();
        this.method = method;
    }

    public Object getInstanceConfigurationClass() {
        return instanceConfigurationClass;
    }

    public void setInstanceConfigurationClass(Object instanceConfigurationClass) {
        this.instanceConfigurationClass = instanceConfigurationClass;
    }

    public Method getMethod() {
        return method;
    }
}
