package com.w1sh.wave.core;

public abstract class AbstractComponentDefinition<T> {

    private final Class<T> clazz;
    private AbstractInjectionPoint<T> injectionPoint;
    private String name;
    private boolean primary;

    protected AbstractComponentDefinition(Class<T> clazz) {
        this.clazz = clazz;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public AbstractInjectionPoint<T> getInjectionPoint() {
        return injectionPoint;
    }

    public void setInjectionPoint(AbstractInjectionPoint<T> injectionPoint) {
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
