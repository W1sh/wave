package com.w1sh.wave.condition;

import com.w1sh.wave.core.annotation.Conditional;
import com.w1sh.wave.core.exception.ComponentCreationException;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GenericFilteringConditionalProcessor implements FilteringConditionalProcessor {

    private final Map<Class<? extends Annotation>, ConditionalProcessor> conditionalProcessorMap = new HashMap<>();
    private final Reflections reflections;

    public GenericFilteringConditionalProcessor(Reflections reflections) {
        this.reflections = reflections;
        initializeProcessors();
    }

    @Override
    public Set<Class<?>> processConditionals(Set<Class<?>> classes) {
        final Set<Class<?>> classesToBeRemoved = new HashSet<>();
        for (Class<?> clazz : classes) {
            final Annotation conditionalAnnotation = getConditionalAnnotation(clazz);
            if (conditionalAnnotation != null) {
                final ConditionalProcessor processor = getProcessor(conditionalAnnotation.annotationType());
                if (!processor.matches(classes, clazz)){
                    classesToBeRemoved.add(clazz);
                }
            }
        }
        classes.removeAll(classesToBeRemoved);
        return classes;
    }

    @Override
    public ConditionalProcessor getProcessor(Class<?> conditionalAnnotation) {
        final var processor = conditionalProcessorMap.getOrDefault(conditionalAnnotation, null);
        if (processor == null) {
            // log and throw
        }
        return processor;
    }

    private void initializeProcessors(){
        final Set<Class<? extends ConditionalProcessor>> subTypesOf = reflections.getSubTypesOf(ConditionalProcessor.class);
        for (Class<? extends ConditionalProcessor> clazz : subTypesOf) {
            if (clazz.isAnnotationPresent(Processor.class)) {
                final Class<? extends Annotation> classOfProcessedAnnotation = clazz.getAnnotation(Processor.class).value();
                final ConditionalProcessor instance = createInstance(clazz);
                conditionalProcessorMap.put(classOfProcessedAnnotation, instance);
            }
        }
    }

    private Annotation getConditionalAnnotation(Class<?> clazz) {
        for (Annotation annotation : clazz.getAnnotations()) {
            if (annotation.annotationType().isAnnotationPresent(Conditional.class)) {
                return annotation;
            }
        }
        return null;
    }

    private ConditionalProcessor createInstance(Class<? extends ConditionalProcessor> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new ComponentCreationException("Unable to create an instance of the class", e);
        }
    }
}
