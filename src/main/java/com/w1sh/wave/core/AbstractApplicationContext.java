package com.w1sh.wave.core;

public abstract class AbstractApplicationContext {

    private final ComponentRegistry registry;
    private final ComponentScanner scanner;
    private AbstractApplicationEnvironment environment;

    protected AbstractApplicationContext(ComponentRegistry registry, ComponentScanner scanner) {
        this.registry = registry;
        this.scanner = scanner;
    }

    public abstract <T> T getComponent(Class<T> clazz);

    public abstract <T> T getComponent(Class<T> clazz, String name);

    public abstract void initialize();

    public abstract void refresh();

    public abstract void clear();

    public AbstractApplicationEnvironment getEnvironment() {
        return environment;
    }

    public void setEnvironment(AbstractApplicationEnvironment environment) {
        this.environment = environment;
    }

    public ComponentRegistry getRegistry() {
        return registry;
    }

    public ComponentScanner getScanner() {
        return scanner;
    }
}
