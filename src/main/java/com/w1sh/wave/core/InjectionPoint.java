package com.w1sh.wave.core;

import java.lang.reflect.Constructor;

public class InjectionPoint<T> extends AbstractInjectionPoint<T> {

    public InjectionPoint(Constructor<T> constructor) {
        super(constructor);
    }

}
