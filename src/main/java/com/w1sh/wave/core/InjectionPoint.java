package com.w1sh.wave.core;

import com.w1sh.wave.core.exception.ComponentCreationException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class InjectionPoint<T> extends AbstractInjectionPoint<T> {

    public InjectionPoint(Constructor<T> constructor) {
        super(constructor);
    }

    @Override
    public T create(Object[] params) {
        try {
            return getConstructor().newInstance(params);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new ComponentCreationException("Unable to create an instance of the class", e);
        }
    }
}
