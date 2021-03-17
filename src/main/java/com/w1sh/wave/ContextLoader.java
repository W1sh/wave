package com.w1sh.wave;

import com.w1sh.wave.annotation.Component;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class ContextLoader {

    private static final Logger logger = LoggerFactory.getLogger(ContextLoader.class);

    private final Reflections reflections;
    private final Injector injector;

    public ContextLoader(Injector injector, String packagePrefix) {
        this.injector = injector;
        this.reflections = new Reflections(packagePrefix);
        Context.initialize();
    }

    protected void loadClassAnnotatedWithComponent(){
        final Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Component.class);
        logger.debug("Found {} classes annotated to be initialized.", classes.size());

        for (Class<?> aClass : classes) {
            Component component = aClass.getAnnotation(Component.class);
            if (!component.lazy()) {
                injector.inject(aClass);
            }
        }
    }
}
