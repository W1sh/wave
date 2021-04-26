package com.w1sh.wave.util;

import com.w1sh.wave.core.AbstractInjectionPoint;
import com.w1sh.wave.core.exception.ComponentCreationException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ReflectionUtils {

    private ReflectionUtils(){}

    public static <T> T newInstance(Class<T> clazz) {
        try {
            return newInstance(clazz.getDeclaredConstructor(), new Object[]{});
        } catch (NoSuchMethodException e) {
            throw new ComponentCreationException("No declared constructor found for class", e);
        }
    }

    public static <T> T newInstance(AbstractInjectionPoint<T> injectionPoint, Object[] params) {
        return newInstance(injectionPoint.getConstructor(), params);
    }

    private static <T> T newInstance(Constructor<T> constructor, Object[] params) {
        try {
            return constructor.newInstance(params);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new ComponentCreationException("Unable to create an instance of the class", e);
        }
    }
}
