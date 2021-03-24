package com.w1sh.wave.core;

import com.w1sh.wave.example.service.impl.CalculatorServiceImpl;
import com.w1sh.wave.example.service.impl.DuplicateCalculatorServiceImpl;
import com.w1sh.wave.example.service.impl.MerchantServiceImpl;
import com.w1sh.wave.example.service.impl.PrimaryCalculatorServiceImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenericComponentDefinitionFactoryTest {

    private final ComponentDefinitionFactory definitionFactory = new GenericComponentDefinitionFactory();

    @Test
    void should_ReturnComponentDefinition_WhenGivenComponentAnnotatedClass(){
        final AbstractComponentDefinition<CalculatorServiceImpl> definition =
                definitionFactory.create(CalculatorServiceImpl.class);

        assertComponentDefinitionIsProperlyDefined(definition);
        assertFalse(definition.isPrimary());
        assertFalse(definition.isLazy());
    }

    @Test
    void should_ReturnComponentDefinition_WhenGivenLazyComponentAnnotatedClass(){
        final AbstractComponentDefinition<DuplicateCalculatorServiceImpl> definition =
                definitionFactory.create(DuplicateCalculatorServiceImpl.class);

        assertComponentDefinitionIsProperlyDefined(definition);
        assertFalse(definition.isPrimary());
        assertTrue(definition.isLazy());
    }

    @Test
    void should_ReturnComponentDefinition_WhenGivenPrimaryComponentAnnotatedClass(){
        final AbstractComponentDefinition<PrimaryCalculatorServiceImpl> definition =
                definitionFactory.create(PrimaryCalculatorServiceImpl.class);

        assertComponentDefinitionIsProperlyDefined(definition);
        assertTrue(definition.isPrimary());
        assertFalse(definition.isLazy());
    }

    @Test
    void should_ReturnComponentDefinition_WhenGivenComponentAnnotatedClassWithDependencies(){
        final AbstractComponentDefinition<MerchantServiceImpl> definition =
                definitionFactory.create(MerchantServiceImpl.class);

        assertComponentDefinitionIsProperlyDefined(definition);
        assertEquals(1, definition.getInjectionPoint().getParameterTypes().length);
        assertFalse(definition.isPrimary());
        assertFalse(definition.isLazy());
    }

    @Test
    void should_ReturnComponentDefinition_WhenGivenComponentAnnotatedClassAndNamed(){
        final AbstractComponentDefinition<CalculatorServiceImpl> definition =
                definitionFactory.create(CalculatorServiceImpl.class, "name");

        assertComponentDefinitionIsProperlyDefined(definition);
        assertEquals(definition.getClazz().getPackageName() + ".name", definition.getName());
        assertFalse(definition.isPrimary());
        assertFalse(definition.isLazy());
    }

    void assertComponentDefinitionIsProperlyDefined(AbstractComponentDefinition<?> definition) {
        assertNotNull(definition);
        assertNotNull(definition.getClazz());
        assertNotNull(definition.getName());
        assertNotNull(definition.getInjectionPoint());
    }

}