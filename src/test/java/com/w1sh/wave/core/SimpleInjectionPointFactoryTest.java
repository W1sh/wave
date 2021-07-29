package com.w1sh.wave.core;

import com.w1sh.wave.core.annotation.Inject;
import com.w1sh.wave.example.service.impl.LazyServiceImpl;
import com.w1sh.wave.example.service.impl.PrimaryCalculatorServiceImpl;
import com.w1sh.wave.example.service.impl.TestConfiguration;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SimpleInjectionPointFactoryTest {

    private final InjectionPointFactory injectionFactory = new SimpleInjectionPointFactory();

    @Test
    void should_ReturnInjectionPoint_WhenGivenClassAnnotatedConstructor(){
        final InjectionPoint injectionPoint = injectionFactory.create(LazyServiceImpl.class.getConstructors()[0]);

        assertTrue(injectionPoint instanceof ConstructorInjectionPoint);
        assertTrue(((ConstructorInjectionPoint) injectionPoint).getConstructor().isAnnotationPresent(Inject.class));
        assertEquals(2, injectionPoint.getParameterTypes().length);
        assertEquals(2, injectionPoint.getParameterAnnotationMetadata().length);
    }

    @Test
    void should_ReturnInjectionPoint_WhenGivenClassDefaultConstructor(){
        final InjectionPoint injectionPoint = injectionFactory.create(PrimaryCalculatorServiceImpl.class.getConstructors()[0]);

        assertTrue(injectionPoint instanceof ConstructorInjectionPoint);
        assertEquals(0, injectionPoint.getParameterTypes().length);
        assertEquals(0, injectionPoint.getParameterAnnotationMetadata().length);
    }

    @Test
    void should_ReturnInjectionPoint_WhenGivenMethodFromConfigurationClass() throws NoSuchMethodException {
        final Method method = TestConfiguration.class.getDeclaredMethod("configurationDefinedMerchantService");
        final InjectionPoint injectionPoint = injectionFactory.create(method);

        assertTrue(injectionPoint instanceof MethodInjectionPoint);
        assertEquals(TestConfiguration.class, ((MethodInjectionPoint) injectionPoint).getMethod().getDeclaringClass());
        assertEquals(0, injectionPoint.getParameterTypes().length);
        assertEquals(0, injectionPoint.getParameterAnnotationMetadata().length);
    }
}