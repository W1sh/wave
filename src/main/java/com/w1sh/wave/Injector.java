package com.w1sh.wave;

import com.w1sh.wave.annotation.Component;
import com.w1sh.wave.annotation.Inject;
import com.w1sh.wave.annotation.Qualifier;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Set;

public class Injector {

    private static final Logger logger = LoggerFactory.getLogger(Injector.class);

    private final Reflections reflections;

    public Injector() {
        this.reflections = new Reflections("com.w1sh.wave");
    }

    protected void getClasses(){
        final Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Component.class);
    }

    protected void inject(Class<?> classToInject){
        if (Context.getComponent(classToInject) != null) {
            logger.debug("Instance of class {} already exists in the context", classToInject.getSimpleName());
            return;
        }

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
        final Class<?>[] parameterTypes = constructor.getParameterTypes();
        final Annotation[][] parameterAnnotations = constructor.getParameterAnnotations();
        final Object[] params = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            if (Arrays.stream(parameterAnnotations[i])
                    .anyMatch(a -> a.annotationType().isAnnotationPresent(Qualifier.class))) {
                // get the qualified component
            }
            params[i] = Context.getComponent(parameterTypes[1]);
        }

    }

    private void createInstance(Class<?> classToInject, Constructor<?> constructor, Object... params) {
        logger.debug("Creating new instance of class {}", classToInject.getSimpleName());
        try {
            final Object instance = constructor.newInstance(params);
            Context.addComponent(classToInject, instance);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
