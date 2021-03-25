package com.w1sh.wave.core;

import com.w1sh.wave.core.annotation.Component;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class GenericComponentScanner implements ComponentScanner {

    private static final Logger logger = LoggerFactory.getLogger(GenericComponentScanner.class);

    private final Reflections reflections;
    private final String packagePrefix;

    public GenericComponentScanner(String packagePrefix) {
        this.reflections = new Reflections(packagePrefix);
        this.packagePrefix = packagePrefix;
    }

    @Override
    public Set<Class<?>> scan() {
        logger.debug("Scanning in defined package \"{}\" for annotated classes", packagePrefix);
        return reflections.getTypesAnnotatedWith(Component.class);
    }
}
