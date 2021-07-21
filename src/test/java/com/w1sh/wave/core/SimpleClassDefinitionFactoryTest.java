package com.w1sh.wave.core;

import com.w1sh.wave.example.service.impl.CalculatorServiceImpl;
import com.w1sh.wave.example.service.impl.DuplicateCalculatorServiceImpl;
import com.w1sh.wave.example.service.impl.MerchantServiceImpl;
import com.w1sh.wave.example.service.impl.PrimaryCalculatorServiceImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimpleClassDefinitionFactoryTest {

    private final ClassDefinitionFactory definitionFactory = new SimpleClassDefinitionFactory();

    @Test
    void should_ReturnComponentDefinition_WhenGivenComponentAnnotatedClass(){
        final Definition definition =
                definitionFactory.create(CalculatorServiceImpl.class);

        assertComponentDefinitionIsProperlyDefined(definition);
        assertFalse(definition.isPrimary());
        assertFalse(definition.isPriority());
    }

    @Test
    void should_ReturnComponentDefinition_WhenGivenLazyComponentAnnotatedClass(){
        final Definition definition =
                definitionFactory.create(DuplicateCalculatorServiceImpl.class);

        assertComponentDefinitionIsProperlyDefined(definition);
        assertFalse(definition.isPrimary());
    }

    @Test
    void should_ReturnComponentDefinition_WhenGivenPrimaryComponentAnnotatedClass(){
        final Definition definition =
                definitionFactory.create(PrimaryCalculatorServiceImpl.class);

        assertComponentDefinitionIsProperlyDefined(definition);
        assertTrue(definition.isPrimary());
    }

    @Test
    void should_ReturnComponentDefinition_WhenGivenComponentAnnotatedClassWithDependencies(){
        final Definition definition =
                definitionFactory.create(MerchantServiceImpl.class);

        assertComponentDefinitionIsProperlyDefined(definition);
        assertEquals(1, definition.getInjectionPoint().getParameterTypes().length);
        assertNotNull(definition.getPriority());
        assertFalse(definition.isPrimary());
    }

    void assertComponentDefinitionIsProperlyDefined(Definition definition) {
        assertNotNull(definition);
        assertNotNull(definition.getClazz());
        assertNotNull(definition.getName());
        assertNotNull(definition.getInjectionPoint());
    }

}