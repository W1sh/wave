package com.w1sh.wave.core;

import com.w1sh.wave.example.service.impl.CalculatorServiceImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class GenericComponentRegistryTest {

    private final ComponentRegistry registry = spy(new GenericComponentRegistry());

    @Test
    void should_ReturnLazyBinding_WhenComponentShouldBeInjectedLazily(){
        Lazy<CalculatorServiceImpl> calculatorServiceLazy = registry.resolveLazy(CalculatorServiceImpl.class);

        final CalculatorServiceImpl calculatorService = calculatorServiceLazy.get();
        verify(registry, times(1)).resolve(CalculatorServiceImpl.class);

        assertNotNull(calculatorServiceLazy);
        assertNotNull(calculatorService);
    }
}