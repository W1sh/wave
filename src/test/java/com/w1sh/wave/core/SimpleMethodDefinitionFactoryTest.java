package com.w1sh.wave.core;

import com.w1sh.wave.example.service.MerchantService;
import com.w1sh.wave.example.service.impl.TestConfiguration;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class SimpleMethodDefinitionFactoryTest {

    private final MethodDefinitionFactory definitionFactory = new SimpleMethodDefinitionFactory();

    @Test
    void should_ReturnComponentDefinition_WhenGivenComponentAnnotatedClass() throws NoSuchMethodException {
        final Method method = TestConfiguration.class.getDeclaredMethod("configurationDefinedMerchantService");
        final Definition definition = definitionFactory.create(method);

        assertNotNull(definition);
        assertNotNull(definition.getInjectionPoint());
        assertEquals(MerchantService.class, definition.getClazz());
        assertEquals("configurationDefinedMerchantService", definition.getName());
        assertFalse(definition.isPrimary());
        assertFalse(definition.isPriority());
    }

}