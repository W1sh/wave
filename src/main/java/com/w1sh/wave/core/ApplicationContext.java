package com.w1sh.wave.core;

import java.util.ArrayList;
import java.util.Set;

public class ApplicationContext extends AbstractApplicationContext {

    public ApplicationContext(ComponentRegistry registry, ComponentScanner scanner) {
        super(registry, scanner);
    }

    public ApplicationContext(ComponentRegistry registry, ComponentScanner scanner, AbstractApplicationEnvironment environment) {
        super(registry, scanner, environment);
    }

    public static ApplicationContextBuilder builder(){
        return new ApplicationContextBuilder();
    }

    @Override
    public <T> T getComponent(Class<T> clazz) {
        return this.getRegistry().getComponent(clazz);
    }

    @Override
    public <T> T getComponent(Class<T> clazz, String name) {
        return this.getRegistry().getComponent(name, clazz);
    }

    @Override
    public boolean containsComponent(Class<?> clazz) {
        return this.getRegistry().getComponent(clazz) != null;
    }

    @Override
    public boolean containsComponent(String name) {
        return this.getRegistry().getComponent(name) != null;
    }

    @Override
    public Class<?> getType(String name) {
        final Object component = this.getRegistry().getComponent(name);
        if (component != null) {
            return component.getClass();
        }
        return null;
    }

    @Override
    public boolean isTypeMatch(String name, Class<?> clazz) {
        final Class<?> type = getType(name);
        return type != null && type.isAssignableFrom(clazz);
    }

    @Override
    public void initialize() {
        final Set<Class<?>> scannedClasses = this.getScanner().scan();
        this.getRegistry().registerMetadata(new ArrayList<>(scannedClasses));
        for (Class<?> scannedClass : scannedClasses) {
            this.getRegistry().register(scannedClass);
        }
    }

    @Override
    public void refresh() {
        this.getRegistry().clear();
        initialize();
    }

    @Override
    public void clear() {
        this.getRegistry().clear();
    }
}
