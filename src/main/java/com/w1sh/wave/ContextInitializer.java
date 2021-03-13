package com.w1sh.wave;

import com.w1sh.wave.annotation.Component;
import com.w1sh.wave.annotation.Inject;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.Set;

public class ContextInitializer {

    private Reflections reflections;

    public ContextInitializer() {
        this.reflections = new Reflections("com.w1sh.wave");
        Context.initialize();
    }

    protected void getClasses(){
        final Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Component.class);
    }

    protected void inject(Class<?> classToInject){
        for (Constructor<?> constructor : classToInject.getConstructors()) {
            if (constructor.isAnnotationPresent(Inject.class)) {
                injectViaConstructor(classToInject, constructor);
                return;
            }
        }
    }

    protected void injectViaConstructor(Class<?> classToInject, Constructor<?> constructor){
        final Class<?>[] parameterTypes = constructor.getParameterTypes();
        final Annotation[][] parameterAnnotations = constructor.getParameterAnnotations();

        for (Class<?> parameterType : parameterTypes) {
            
        }
    }

}
