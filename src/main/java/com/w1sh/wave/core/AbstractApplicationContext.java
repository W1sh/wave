package com.w1sh.wave.core;

public abstract class AbstractApplicationContext {

    private final ComponentDefinitionFactory factory;
    private final ComponentRegistry registry;
    private final ComponentScanner scanner;
    private AbstractApplicationEnvironment environment;

    protected AbstractApplicationContext(ComponentDefinitionFactory factory, ComponentRegistry registry, ComponentScanner scanner) {
        this.factory = factory;
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

    public ComponentDefinitionFactory getFactory() {
        return factory;
    }

    public ComponentRegistry getRegistry() {
        return registry;
    }

    public ComponentScanner getScanner() {
        return scanner;
    }
}
