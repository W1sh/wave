package com.w1sh.wave.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;

public class SimpleInjectionPointFactory implements InjectionPointFactory {

    @Override
    public InjectionPoint create(Executable executable) {
        final InjectionPoint injectionPoint = executable instanceof Constructor ?
                new ConstructorInjectionPoint((Constructor<?>) executable) : new MethodInjectionPoint(((Method) executable));

        if (executable.getParameterCount() == 0) {
            return injectionPoint;
        }

        final var parameterTypes = executable.getGenericParameterTypes();
        final var parametersAnnotationMetadata = new ParameterAnnotationMetadata[executable.getParameterCount()];

        for (var i = 0; i < parameterTypes.length; i++) {
            final var annotationMetadata = new ParameterAnnotationMetadata();
            annotationMetadata.addAll(executable.getParameterAnnotations()[i]);
        }

        injectionPoint.setParameterTypes(parameterTypes);
        injectionPoint.setParameterAnnotationMetadata(parametersAnnotationMetadata);
        return injectionPoint;
    }
}
