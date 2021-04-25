package com.w1sh.wave.condition;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GenericFilteringConditionProcessor implements FilteringConditionProcessor {

    @Override
    public Set<Class<?>> processConditionals(Set<Class<?>> classes) {
        final Set<Class<?>> conditionalOnComponents = filterByAnnotation(ConditionalOnComponent.class, classes);
        final Set<Class<?>> conditionalOnMissingComponents = filterByAnnotation(ConditionalOnMissingComponent.class, classes);

        final Set<Class<?>> notMatchingOnComponents = processOnComponent(conditionalOnComponents, classes);
        final Set<Class<?>> notMatchingOnMissingComponent = processOnMissingComponent(conditionalOnMissingComponents, classes);

        classes.removeAll(notMatchingOnComponents);
        classes.removeAll(notMatchingOnMissingComponent);

        return classes;
    }

    private Set<Class<?>> processOnComponent(Set<Class<?>> conditionalClasses, Set<Class<?>> classes) {
        final Set<Class<?>> notMatchingClasses = new HashSet<>();
        for (Class<?> conditionalClass : conditionalClasses) {
            ConditionalOnComponent onComponent = conditionalClass.getAnnotation(ConditionalOnComponent.class);
            if (!classes.containsAll(List.of(onComponent.value()))) {
                notMatchingClasses.add(conditionalClass);
            }
        }
        return notMatchingClasses;
    }

    private Set<Class<?>> processOnMissingComponent(Set<Class<?>> conditionalClasses, Set<Class<?>> classes) {
        final Set<Class<?>> notMatchingClasses = new HashSet<>();
        for (Class<?> conditionalClass : conditionalClasses) {
            ConditionalOnMissingComponent onMissingComponent = conditionalClass.getAnnotation(ConditionalOnMissingComponent.class);
            if (classes.containsAll(List.of(onMissingComponent.value()))) {
                notMatchingClasses.add(conditionalClass);
            }
        }
        return notMatchingClasses;
    }

    private <T extends Annotation> Set<Class<?>> filterByAnnotation(Class<T> clazz, Set<Class<?>> classes) {
        return classes.stream()
                .filter(c -> c.isAnnotationPresent(clazz))
                .collect(Collectors.toSet());
    }
}
