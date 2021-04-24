package com.w1sh.wave.core;

import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public class ProviderBinding<T> implements Provider<T>{

    private final Supplier<T> supplier;

    public ProviderBinding(Class<T> clazz, ComponentRegistry context) {
        this.supplier = () -> context.getComponent(clazz);
    }

    public ProviderBinding(Class<T> clazz, String name, ComponentRegistry context) {
        this.supplier = () -> context.getComponent(name, clazz);
    }

    @Override
    public T get() {
        return requireNonNull(supplier.get());
    }
}
