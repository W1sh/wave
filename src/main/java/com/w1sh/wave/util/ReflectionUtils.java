package com.w1sh.wave.util;

import com.w1sh.wave.core.InjectionPoint;
import com.w1sh.wave.core.ConstructorInjectionPoint;
import com.w1sh.wave.core.MethodInjectionPoint;
import com.w1sh.wave.core.exception.ComponentCreationException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtils {

    private ReflectionUtils(){}

    public static <T> T newInstance(Class<T> clazz) {
        try {
            return newInstance(clazz.getDeclaredConstructor(), new Object[]{});
        } catch (NoSuchMethodException e) {
            throw new ComponentCreationException("No declared constructor found for class", e);
        }
    }

    public static <T> T newInstance(InjectionPoint injectionPoint, Object[] params) {
        if (injectionPoint instanceof MethodInjectionPoint) {
            return newInstance(((MethodInjectionPoint) injectionPoint).getMethod(),
                    ((MethodInjectionPoint) injectionPoint).getInstanceConfigurationClass(), params);
        } else {
            return newInstance(((ConstructorInjectionPoint) injectionPoint).getConstructor(), params);
        }
    }

    @SuppressWarnings("unchecked")
    private static  <T> T newInstance(Method method, Object instance, Object[] params) {
        try {
            return (T) method.invoke(instance, params);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new ComponentCreationException("Failed to invoke method", e);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T newInstance(Constructor<?> constructor, Object[] params) {
        try {
            return (T) constructor.newInstance(params);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new ComponentCreationException("Unable to create an instance of the class", e);
        }
    }
}
