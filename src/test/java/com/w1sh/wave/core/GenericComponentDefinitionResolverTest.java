package com.w1sh.wave.core;

import com.w1sh.wave.example.service.CalculatorService;
import com.w1sh.wave.example.service.impl.CalculatorServiceImpl;
import com.w1sh.wave.example.service.impl.MerchantServiceImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class GenericComponentDefinitionResolverTest {

    private final ComponentRegistry registry = mock(ComponentRegistry.class);
    private final ComponentDefinitionFactory factory = new GenericComponentDefinitionFactory();
    private final ComponentDefinitionResolver resolver = new GenericComponentDefinitionResolver(registry);

    @Test
    void should_ReturnObject_WhenGivenComponentDefinition(){
        final AbstractComponentDefinition<CalculatorServiceImpl> definition = factory.create(CalculatorServiceImpl.class);

        final Object object = resolver.resolve(definition);

        verify(registry, never()).register((Class<Object>) any());
        verify(registry, never()).register(any(), any());
        assertNotNull(object);
    }
}