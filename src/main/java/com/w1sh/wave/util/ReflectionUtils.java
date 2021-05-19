package com.w1sh.wave.util;

import com.w1sh.wave.core.ConstructorInjectionPoint;
import com.w1sh.wave.core.InjectionPoint;
import com.w1sh.wave.core.MethodInjectionPoint;
import com.w1sh.wave.core.annotation.Nullable;
import com.w1sh.wave.core.annotation.Qualifier;
import com.w1sh.wave.core.exception.ComponentCreationException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtils {

    private ReflectionUtils(){}

    public static InjectionPoint injectionPointFromExecutable(Executable executable) {
        final InjectionPoint injectionPoint = executable instanceof Constructor ?
                new ConstructorInjectionPoint((Constructor<?>) executable) : new MethodInjectionPoint(((Method) executable));
        if (executable.getParameterCount() == 0) {
            return injectionPoint;
        }

        final var parameterTypes = executable.getGenericParameterTypes();
        final var qualifiers = new Qualifier[executable.getParameterCount()];
        final var nullables = new Nullable[executable.getParameterCount()];

        for (var i = 0; i < parameterTypes.length; i++) {
            for (Annotation annotation : executable.getParameterAnnotations()[i]) {
                if (annotation instanceof Qualifier) {
                    qualifiers[i] = (Qualifier) annotation;
                } else if (annotation instanceof Nullable) {
                    nullables[i] = (Nullable) annotation;
                }
            }
        }
        injectionPoint.setParameterTypes(parameterTypes);
        injectionPoint.setQualifiers(qualifiers);
        injectionPoint.setNullables(nullables);
        return injectionPoint;
    }

    public static <T> T newInstance(Class<T> clazz) {
        try {
            return newInstance(clazz.getDeclaredConstructor(), new Object[]{});
        } catch (NoSuchMethodException e) {
            throw new ComponentCreationException("No declared constructor found for class", e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T newInstance(InjectionPoint injectionPoint, Object[] params) {
        if (injectionPoint instanceof MethodInjectionPoint) {
            return (T) newInstance(((MethodInjectionPoint) injectionPoint).getMethod(),
                    ((MethodInjectionPoint) injectionPoint).getInstanceConfigurationClass(), params);
        } else {
            return (T) newInstance(((ConstructorInjectionPoint) injectionPoint).getConstructor(), params);
        }
    }

    private static Object newInstance(Method method, Object instance, Object[] params) {
        try {
            return method.invoke(instance, params);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new ComponentCreationException("Failed to invoke method", e);
        }
    }

    private static <T> T newInstance(Constructor<T> constructor, Object[] params) {
        try {
            return constructor.newInstance(params);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new ComponentCreationException("Unable to create an instance of the class", e);
        }
    }
}
