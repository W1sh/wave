package com.w1sh.wave.core.binding;

import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public class ProviderBinding<T> implements Provider<T>{

    private final Supplier<T> supplier;

    public ProviderBinding(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    @Override
    public T get() {
        return requireNonNull(supplier.get());
    }
}
