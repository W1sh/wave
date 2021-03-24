package com.w1sh.wave.core;

public abstract class AbstractComponentDefinition {

    private final Class<?> clazz;
    private AbstractInjectionPoint injectionPoint;
    private String name;
    private boolean primary;
    private boolean lazy;

    protected AbstractComponentDefinition(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public AbstractInjectionPoint getInjectionPoint() {
        return injectionPoint;
    }

    public void setInjectionPoint(AbstractInjectionPoint injectionPoint) {
        this.injectionPoint = injectionPoint;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public boolean isLazy() {
        return lazy;
    }

    public void setLazy(boolean lazy) {
        this.lazy = lazy;
    }
}
