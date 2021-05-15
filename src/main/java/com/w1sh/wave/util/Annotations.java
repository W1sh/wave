package com.w1sh.wave.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Annotations {

    private Annotations(){}

    public static boolean isAnnotationPresent(AnnotatedElement annotatedElement, Class<? extends Annotation> annotation) {
        return Arrays.stream(annotatedElement.getAnnotations())
                .anyMatch(a -> a.annotationType().equals(annotation) || a.annotationType().isAnnotationPresent(annotation));
    }

    public static List<Annotation> getAnnotationsOfType(AnnotatedElement annotatedElement, Class<? extends Annotation> annotation) {
        return Arrays.stream(annotatedElement.getAnnotations())
                .filter(a -> a.annotationType().equals(annotation) || a.annotationType().isAnnotationPresent(annotation))
                .collect(Collectors.toList());
    }
}
