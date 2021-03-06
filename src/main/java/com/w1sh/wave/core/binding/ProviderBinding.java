package com.w1sh.wave.core.binding;

import com.w1sh.wave.core.ObjectProvider;

import static java.util.Objects.requireNonNull;

public class ProviderBinding<T> implements Provider<T> {

    private final ObjectProvider<T> provider;

    public ProviderBinding(ObjectProvider<T> provider) {
        this.provider = provider;
    }

    @Override
    public T get() {
        return requireNonNull(provider.newInstance());
    }
}
