package com.w1sh.wave.core;

import com.w1sh.wave.core.annotation.Component;

public interface ComponentNameGenerator {

    String generate(Class<?> aClass, Component component);
}
