package com.w1sh.wave;

import com.w1sh.wave.example.service.impl.BetterCalculatorServiceImpl;
import com.w1sh.wave.example.service.impl.CalculatorServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InjectorTest {

    private Injector injector;

    @BeforeEach
    void setUp() {
        injector = new Injector();
        Context.initialize();
    }

    @Test
    void should_CreateNewInstance_WhenNoneIsPresentInContext(){
        final CalculatorServiceImpl existingComponent = Context.getComponent(CalculatorServiceImpl.class);
        injector.inject(CalculatorServiceImpl.class);

        final CalculatorServiceImpl component = Context.getComponent(CalculatorServiceImpl.class);

        Assertions.assertNull(existingComponent);
        Assertions.assertNotNull(component);
    }

    @Test
    void should_NotAttemptInject_WhenInstanceIsPresentInContext(){
        final CalculatorServiceImpl existingComponent = Context.getComponent(CalculatorServiceImpl.class);
        injector.inject(CalculatorServiceImpl.class);

        final CalculatorServiceImpl component = Context.getComponent(CalculatorServiceImpl.class);

        Assertions.assertNotNull(existingComponent);
        Assertions.assertNotNull(component);
        Assertions.assertEquals(existingComponent, component);
    }

    @Test
    void should_StoreComponentUsingQualifier_WhenQualifierAnnotationIsPresent(){
        final BetterCalculatorServiceImpl existingComponent = Context.getComponent(BetterCalculatorServiceImpl.class);
        injector.inject(BetterCalculatorServiceImpl.class);

        final BetterCalculatorServiceImpl component = Context.getComponent(
                BetterCalculatorServiceImpl.class, "betterCalculatorService");

        Assertions.assertNull(existingComponent);
        Assertions.assertNotNull(component);
    }
}
