package com.w1sh.wave.core;

public abstract class Definition {

    private final Class<?> clazz;
    private InjectionPoint injectionPoint;
    private String name;
    private boolean primary;
    private boolean conditional;
    private DefinitionStatus status = DefinitionStatus.PENDING;

    protected Definition(Class<?> clazz) {
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

    public boolean isConditional() {
        return conditional;
    }

    public void setConditional(boolean conditional) {
        this.conditional = conditional;
    }

    public boolean isProcessed() {
        return DefinitionStatus.PROCESSED.equals(status);
    }

    public void setProcessed() {
        this.status = DefinitionStatus.PROCESSED;
    }

    public boolean isResolved() {
        return DefinitionStatus.RESOLVED.equals(status);
    }

    public void setResolved() {
        this.status = DefinitionStatus.RESOLVED;
    }

    private enum DefinitionStatus {
        PENDING, PROCESSED, RESOLVED
    }
}
