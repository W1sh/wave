package com.w1sh.wave.core;

public abstract class AbstractApplicationContext implements Configurable {

    private final ClassDefinitionFactory classDefinitionFactory;
    private final MethodDefinitionFactory methodDefinitionFactory;
    private final ComponentRegistry registry;
    private final ComponentScanner scanner;
    private AbstractApplicationEnvironment environment;

    protected AbstractApplicationContext(ComponentRegistry registry, ComponentScanner scanner,
                                         ClassDefinitionFactory classDefinitionFactory, MethodDefinitionFactory methodDefinitionFactory) {
        this.registry = registry;
        this.scanner = scanner;
        this.environment = ApplicationEnvironment.builder().build();
        this.classDefinitionFactory = classDefinitionFactory;
        this.methodDefinitionFactory = methodDefinitionFactory;
    }

    protected AbstractApplicationContext(ComponentRegistry registry, ComponentScanner scanner,
                                         AbstractApplicationEnvironment environment, ClassDefinitionFactory classDefinitionFactory,
                                         MethodDefinitionFactory methodDefinitionFactory) {
        this(registry, scanner, classDefinitionFactory, methodDefinitionFactory);
        this.environment = environment;
    }

    public abstract <T> T getComponent(Class<T> clazz);

    public abstract <T> T getComponent(Class<T> clazz, String name);

    public abstract boolean containsComponent(Class<?> clazz);

    public abstract boolean containsComponent(String name);

    public abstract Class<?> getType(String name);

    public abstract boolean isTypeMatch(String name, Class<?> clazz);

    public abstract void initialize();

    public abstract void refresh();

    public abstract void clear();

    public AbstractApplicationEnvironment getEnvironment() {
        return environment;
    }

    public void setEnvironment(AbstractApplicationEnvironment environment) {
        this.environment = environment;
        this.registry.setEnvironment(environment);
    }

    public ClassDefinitionFactory getClassDefinitionFactory() {
        return classDefinitionFactory;
    }

    public MethodDefinitionFactory getMethodDefinitionFactory() {
        return methodDefinitionFactory;
    }

    public ComponentRegistry getRegistry() {
        return registry;
    }

    public ComponentScanner getScanner() {
        return scanner;
    }
}
