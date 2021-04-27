package com.w1sh.wave.core;

public abstract class Definition<T> {

    private final Class<T> clazz;
    private InjectionPoint injectionPoint;
    private String name;
    private boolean primary;

    protected Definition(Class<T> clazz) {
        this.clazz = clazz;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public InjectionPoint getInjectionPoint() {
        return injectionPoint;
    }

    public void setInjectionPoint(InjectionPoint injectionPoint) {
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
}
