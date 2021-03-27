package com.w1sh.wave.core;

import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public class LazyBinding<T> implements Lazy<T> {

    private final Supplier<T> supplier;
    private volatile T delegate;

    public LazyBinding(Class<T> clazz, ComponentRegistry registry) {
        this.supplier = () -> registry.resolve(clazz);
    }

    @Override
    public synchronized T get() {
        if (delegate == null) {
            delegate = requireNonNull(supplier.get());
        }
        return delegate;
    }
}
