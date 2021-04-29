package com.w1sh.wave.core;

import java.lang.reflect.Method;
import java.util.Set;

public interface ComponentScanner {

    Set<Class<?>> scanClasses();

    Set<Method> scanMethods();

    void ignoreType(Class<?> clazz);
}
