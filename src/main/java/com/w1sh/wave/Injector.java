package com.w1sh.wave;

import com.w1sh.wave.annotation.Inject;
import com.w1sh.wave.annotation.Qualifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Injector {

    private static final Logger logger = LoggerFactory.getLogger(Injector.class);

    protected void inject(Class<?> classToInject){
        for (Constructor<?> constructor : classToInject.getConstructors()) {
            if (constructor.isAnnotationPresent(Inject.class)) {
                if (constructor.getParameterCount() == 0) {
                    logger.debug("Constructor for class {} does not require any dependencies.",
                            classToInject.getSimpleName());
                    createInstance(classToInject, constructor);
                    return;
                }
                logger.debug("Injecting dependencies into new instance of class {}", classToInject.getSimpleName());
                injectViaConstructor(classToInject, constructor);
                return;
            }
        }

        // If no annotated constructors were found, create instance using the default constructor
        createInstance(classToInject, classToInject.getConstructors()[0]);
    }

    protected void injectViaConstructor(Class<?> classToInject, Constructor<?> constructor){
        final Class<?>[] paramTypes = constructor.getParameterTypes();
        final Qualifier[] qualifiers = new Qualifier[paramTypes.length];
        final Annotation[][] paramAnnotations = constructor.getParameterAnnotations();
        final Object[] params = new Object[paramTypes.length];

        for (int i = 0; i < paramAnnotations.length; i++) {
            for (Annotation annotation : paramAnnotations[i]) {
                if (annotation instanceof Qualifier) {
                    qualifiers[i] = (Qualifier) annotation;
                    break;
                }
            }
        }

        for (int i = 0; i < paramTypes.length; i++) {
            params[i] = qualifiers[i] != null ? Context.getComponent(paramTypes[i], qualifiers[i].name()) :
                    Context.getComponent(paramTypes[i]);
        }

        createInstance(classToInject, constructor, params);
    }

    private void createInstance(Class<?> classToInject, Constructor<?> constructor, Object... params) {
        logger.debug("Creating new instance of class {}", classToInject.getSimpleName());
        try {
            final Object instance = constructor.newInstance(params);
            Context.addComponent(classToInject, instance);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            logger.error("Unable to create an instance of the class {}", classToInject, e);
        }
    }
}
