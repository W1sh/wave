package com.w1sh.wave.core;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ApplicationContext extends AbstractApplicationContext {

    public ApplicationContext(ComponentDefinitionFactory factory, ComponentRegistry registry, ComponentScanner scanner) {
        super(factory, registry, scanner);
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
    public void initialize() {
        final Set<Class<?>> scannedClasses = this.getScanner().scan();
        final List<AbstractComponentDefinition<?>> definitions = scannedClasses.stream()
                .map(c -> this.getFactory().create(c))
                .collect(Collectors.toList());

        this.getRegistry().registerDefinitions(definitions);

        this.getRegistry().register(scannedClasses);
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
