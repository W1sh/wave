package com.w1sh.wave.core;

import com.w1sh.wave.core.annotation.Component;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class GenericComponentScanner implements ComponentScanner {

    private static final Logger logger = LoggerFactory.getLogger(GenericComponentScanner.class);

    private final Reflections reflections;

    public GenericComponentScanner(String packagePrefix) {
        this.reflections = new Reflections(packagePrefix);
    }

    @Override
    public Set<Class<?>> scan() {
        return reflections.getTypesAnnotatedWith(Component.class);
    }
}
