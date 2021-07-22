package com.w1sh.wave.core;

import com.w1sh.wave.core.annotation.Component;

public class SimpleComponentNameGenerator implements ComponentNameGenerator {

    @Override
    public String generate(Class<?> aClass, Component component) {
        if (component != null && !component.name().isBlank()) {
            return aClass.getPackageName() + "." + component.name();
        } else {
            return aClass.getPackageName() + "." + aClass.getSimpleName();
        }
    }
}
