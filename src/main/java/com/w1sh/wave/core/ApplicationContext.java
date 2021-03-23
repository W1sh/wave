package com.w1sh.wave.core;

import java.util.Set;

public class ApplicationContext extends AbstractApplicationContext {

    public ApplicationContext(ComponentRegistry registry, ComponentScanner scanner) {
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
        final Set<Class<?>> scannedClasses = this.getScanner().scan();
        for (Class<?> scannedClass : scannedClasses) {
            this.getRegistry().register(scannedClass);
        }
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
