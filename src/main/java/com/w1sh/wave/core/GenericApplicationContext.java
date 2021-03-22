package com.w1sh.wave.core;

import java.util.Set;

public class GenericApplicationContext extends AbstractApplicationContext {

    public GenericApplicationContext(ComponentRegistry registry, ComponentScanner scanner) {
        super(registry, scanner);
    }

    @Override
    public <T> T getComponent(Class<T> clazz) {
        return this.getRegistry().getComponent(clazz);
    }

    @Override
    public <T> T getComponent(Class<T> clazz, String name) {
        return this.getRegistry().getComponent(clazz, name);
    }

    @Override
    public void initialize() {
        final Set<AbstractComponentDefinition> scannedDefinitions = this.getScanner().scan();
        this.getRegistry().fillWithComponentMetadata(scannedDefinitions);
        this.getRegistry().register();
        this.getRegistry().clearComponentMetadata();
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
