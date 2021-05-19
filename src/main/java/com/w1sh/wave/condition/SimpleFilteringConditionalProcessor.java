package com.w1sh.wave.condition;

import com.w1sh.wave.core.ApplicationContext;
import com.w1sh.wave.core.Definition;
import com.w1sh.wave.core.annotation.Conditional;
import com.w1sh.wave.core.exception.UnresolvableConditionalException;
import com.w1sh.wave.util.Annotations;
import com.w1sh.wave.util.ReflectionUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SimpleFilteringConditionalProcessor implements FilteringConditionalProcessor {

    private static final Logger logger = LoggerFactory.getLogger(SimpleFilteringConditionalProcessor.class);

    private final Map<Class<? extends Annotation>, ConditionalProcessor> conditionalProcessorMap = new HashMap<>();
    private final Reflections reflections;

    public SimpleFilteringConditionalProcessor(Reflections reflections) {
        this.reflections = reflections;
        initializeProcessors();
    }

    @Override
    public boolean evaluate(ApplicationContext context, Definition definitionToProcess) {
        return matches(context, definitionToProcess);
    }

    @Override
    public ConditionalProcessor getProcessor(Class<?> conditionalAnnotation) {
        final var processor = conditionalProcessorMap.getOrDefault(conditionalAnnotation, null);
        if (processor == null) {
            logger.error("Unable to find a conditional processor for the annotation class {}", conditionalAnnotation);
            throw new UnresolvableConditionalException("Unable to find a conditional processor for the annotation class "
                    + conditionalAnnotation);
        }
        return processor;
    }

    private boolean matches(ApplicationContext context, Definition definition) {
        final List<Annotation> conditionalAnnotation = Annotations.getAnnotationsOfType(definition.getClazz(), Conditional.class);
        for (Annotation annotation : conditionalAnnotation) {
            final ConditionalProcessor processor = getProcessor(annotation.annotationType());
            if (!processor.matches(context, definition.getClazz())) {
                return false;
            }
        }
        return true;
    }

    private void initializeProcessors() {
        final Set<Class<? extends ConditionalProcessor>> subTypesOf = reflections.getSubTypesOf(ConditionalProcessor.class);
        for (Class<? extends ConditionalProcessor> clazz : subTypesOf) {
            if (clazz.isAnnotationPresent(Processor.class)) {
                final Class<? extends Annotation> classOfProcessedAnnotation = clazz.getAnnotation(Processor.class).value();
                final ConditionalProcessor instance = ReflectionUtils.newInstance(clazz);
                conditionalProcessorMap.put(classOfProcessedAnnotation, instance);
            }
        }
    }
}
