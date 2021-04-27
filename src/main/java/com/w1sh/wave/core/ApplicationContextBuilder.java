package com.w1sh.wave.core;

public class ApplicationContextBuilder {

    private ClassDefinitionFactory classDefinitionFactory;
    private MethodDefinitionFactory methodDefinitionFactory;
    private ComponentRegistry registry;
    private ComponentScanner scanner;
    private AbstractApplicationEnvironment environment;

    public ApplicationContextBuilder setRegistry(ComponentRegistry registry) {
        this.registry = registry;
        return this;
    }

    public ApplicationContextBuilder setScanner(ComponentScanner scanner) {
        this.scanner = scanner;
        return this;
    }

    public ApplicationContextBuilder setEnvironment(AbstractApplicationEnvironment environment) {
        this.environment = environment;
        return this;
    }

    public ApplicationContextBuilder setClassDefinitionFactory(ClassDefinitionFactory definitionFactory) {
        this.classDefinitionFactory = definitionFactory;
        return this;
    }

    public ApplicationContextBuilder setMethodDefinitionFactory(MethodDefinitionFactory definitionFactory) {
        this.methodDefinitionFactory = definitionFactory;
        return this;
    }

    public ApplicationContext build() {
        return new ApplicationContext(registry, scanner, environment, classDefinitionFactory, methodDefinitionFactory);
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

    public AbstractApplicationEnvironment getEnvironment() {
        return environment;
    }
}
