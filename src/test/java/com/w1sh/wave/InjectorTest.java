package com.w1sh.wave;

import com.w1sh.wave.core.exception.ComponentCreationException;
import com.w1sh.wave.example.ConstructorInjectionWithQualifierExample;
import com.w1sh.wave.example.service.CalculatorService;
import com.w1sh.wave.example.service.impl.BetterCalculatorServiceImpl;
import com.w1sh.wave.example.service.impl.CalculatorServiceImpl;
import com.w1sh.wave.example.service.impl.MerchantServiceImpl;
import com.w1sh.wave.example.service.impl.PrimaryCalculatorServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InjectorTest {

    private Injector injector;

    @BeforeAll
    static void beforeAll() {
        Context.initialize();
    }

    @BeforeEach
    void setUp() {
        injector = new Injector();
        Context.clearContext();
    }

    @Test
    void should_CreateNewInstance_WhenNoneIsPresentInContext(){
        Assertions.assertThrows(ComponentCreationException.class, () -> {
            Context.getComponent(BetterCalculatorServiceImpl.class);
        });

        injector.inject(CalculatorServiceImpl.class);

        final CalculatorServiceImpl component = Context.getComponent(CalculatorServiceImpl.class);

        Assertions.assertNotNull(component);
    }

    @Test
    void should_NotAttemptInject_WhenInstanceIsPresentInContext(){
        injector.inject(CalculatorServiceImpl.class);
        final CalculatorServiceImpl existingComponent = Context.getComponent(CalculatorServiceImpl.class);

        injector.inject(CalculatorServiceImpl.class);
        final CalculatorServiceImpl component = Context.getComponent(CalculatorServiceImpl.class);

        Assertions.assertNotNull(existingComponent);
        Assertions.assertNotNull(component);
        Assertions.assertEquals(existingComponent, component);
    }

    @Test
    void should_StoreComponentUsingQualifier_WhenQualifierAnnotationIsPresent(){
        Assertions.assertThrows(ComponentCreationException.class, () -> {
            Context.getComponent(BetterCalculatorServiceImpl.class);
        });

        injector.inject(BetterCalculatorServiceImpl.class);

        final BetterCalculatorServiceImpl component = Context.getComponent(
                BetterCalculatorServiceImpl.class, "betterCalculatorService");

        Assertions.assertNotNull(component);
    }

    @Test
    void should_CreateNewInstance_WhenNoneIsPresentAndAllConstructorParamsPresentInContext(){
        injector.inject(CalculatorServiceImpl.class);
        injector.inject(MerchantServiceImpl.class);

        final MerchantServiceImpl component = Context.getComponent(MerchantServiceImpl.class);

        Assertions.assertNotNull(component);
        Assertions.assertNotNull(component.getCalculatorService());
    }

    @Test
    void should_CreateNewInstanceWithQualifierParameters_WhenNoneIsPresentAndAllConstructorParamsPresentInContext(){
        injector.inject(CalculatorServiceImpl.class);
        injector.inject(MerchantServiceImpl.class);
        injector.inject(ConstructorInjectionWithQualifierExample.class);

        final ConstructorInjectionWithQualifierExample component = Context.getComponent(ConstructorInjectionWithQualifierExample.class);

        Assertions.assertNotNull(component);
        Assertions.assertNotNull(component.getMerchantService());
        Assertions.assertNotNull(component.getCalculatorService());
    }

    @Test
    void should_InjectPrimaryComponent_WhenMultipleComponentsArePresentInContextForGivenClass(){
        injector.inject(CalculatorServiceImpl.class);
        injector.inject(PrimaryCalculatorServiceImpl.class);

        final CalculatorService component = Context.getComponent(CalculatorService.class);

        Assertions.assertNotNull(component);
        Assertions.assertEquals(PrimaryCalculatorServiceImpl.class, component.getClass());
    }
}
