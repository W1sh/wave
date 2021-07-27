package com.w1sh.wave.core.binding;

import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public class LazyBinding<T> implements Lazy<T> {

    private final Supplier<T> supplier;
    private volatile T delegate;

    public LazyBinding(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    @Override
    public synchronized T get() {
        if (delegate == null) {
            delegate = requireNonNull(supplier.get());
        }
        return delegate;
    }
}
