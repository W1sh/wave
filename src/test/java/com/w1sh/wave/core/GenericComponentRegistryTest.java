package com.w1sh.wave.core;

import com.w1sh.wave.example.service.impl.BetterCalculatorServiceImpl;
import com.w1sh.wave.example.service.impl.CalculatorServiceImpl;
import com.w1sh.wave.example.service.impl.LazyServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class GenericComponentRegistryTest {

    private final ComponentRegistry registry = new GenericComponentRegistry();

    @Test
    void should_registerMetadata_whenGivenListOfClasses(){
        /*registry.registerMetadata(List.of(LazyServiceImpl.class, BetterCalculatorServiceImpl.class, CalculatorServiceImpl.class));
        verify(factory, times(3)).create(any());*/
    }

    @Test
    void should_returnInstance_whenClassHasBeenRegistered(){
        /*registry.registerMetadata(List.of(LazyServiceImpl.class, BetterCalculatorServiceImpl.class, CalculatorServiceImpl.class));
        registry.register(LazyServiceImpl.class);
        final LazyServiceImpl component = registry.getComponent(LazyServiceImpl.class);

        assertNotNull(component);
        assertNotNull(component.getCalculatorService());
        assertNotNull(component.getBetterCalculatorService());*/
    }
}