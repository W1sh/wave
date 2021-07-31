package com.w1sh.wave.core;

import com.w1sh.wave.core.annotation.Priority;

import java.lang.reflect.Method;
import java.util.List;

public abstract class Definition {

    private final Class<?> clazz;
    private InjectionPoint injectionPoint;
    private String name;
    private String[] aliases;
    private boolean primary;
    private boolean conditional;
    private Priority priority;
    private List<Method> postConstructorMethods;
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

    public Priority getPriority() {
        return priority;
    }

    public boolean isPriority() {
        return priority != null;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public List<Method> getPostConstructorMethods() {
        return postConstructorMethods;
    }

    public void setPostConstructorMethods(List<Method> postConstructorMethods) {
        this.postConstructorMethods = postConstructorMethods;
    }

    public String[] getAliases() {
        return aliases != null ? aliases : new String[0];
    }

    public void setAliases(String[] aliases) {
        this.aliases = aliases;
    }

    private enum DefinitionStatus {
        PENDING, PROCESSED, RESOLVED
    }
}
