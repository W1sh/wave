package com.w1sh.wave.core;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public abstract class AnnotationMetadata {

    private final Map<Class<? extends Annotation>, Annotation> annotationMap = new ConcurrentHashMap<>(8);

    public void add(Annotation annotation) {
        annotationMap.put(annotation.getClass(), annotation);
    }

    public void addAll(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            add(annotation);
        }
    }

    public Annotation get(Class<? extends Annotation> annotationClass) {
        return annotationMap.getOrDefault(annotationClass, null);
    }

    public boolean hasAnnotation(Class<? extends Annotation> annotationClass) {
        return annotationMap.containsKey(annotationClass);
    }

    public List<Annotation> getAnnotations() {
        return new ArrayList<>(annotationMap.values());
    }

    public Set<String> getAnnotationTypes() {
        return annotationMap.values().stream()
                .map(annotation -> annotation.getClass().getSimpleName())
                .collect(Collectors.toSet());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnnotationMetadata that = (AnnotationMetadata) o;
        return Objects.equals(annotationMap, that.annotationMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(annotationMap);
    }
}
