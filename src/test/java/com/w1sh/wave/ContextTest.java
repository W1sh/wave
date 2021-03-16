package com.w1sh.wave;

import com.w1sh.wave.example.service.impl.BetterCalculatorServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContextTest {

    @BeforeEach
    void setUp() {
        Context.initialize();
    }

    @Test
    void should_ThrowException_WhenNoComponentIsPresentInContextForGivenClass(){
        ComponentCreationException exception = Assertions.assertThrows(ComponentCreationException.class, () -> {
            Context.getComponent(BetterCalculatorServiceImpl.class);
        });

        Assertions.assertNotNull(exception);
        Assertions.assertNotNull(exception.getMessage());
    }

    @Test
    void should_ThrowException_WhenMultipleCandidatesArePresentInContextForGivenClass(){
        /*ComponentCreationException exception = Assertions.assertThrows(ComponentCreationException.class, () -> {
            Context.getComponent(BetterCalculatorServiceImpl.class);
        });

        Assertions.assertNotNull(exception);
        Assertions.assertNotNull(exception.getMessage());*/
    }

    @Test
    void should_ThrowException_WhenNoComponentIsPresentInContextForGivenClassAndName(){
        ComponentCreationException exception = Assertions.assertThrows(ComponentCreationException.class, () -> {
            Context.getComponent(BetterCalculatorServiceImpl.class, "betterCalculatorService");
        });

        Assertions.assertNotNull(exception);
        Assertions.assertNotNull(exception.getMessage());
    }

    @Test
    void should_ThrowException_WhenMultipleCandidatesArePresentInContextForGivenClassAndName(){
        /*ComponentCreationException exception = Assertions.assertThrows(ComponentCreationException.class, () -> {
            Context.getComponent(BetterCalculatorServiceImpl.class);
        });

        Assertions.assertNotNull(exception);
        Assertions.assertNotNull(exception.getMessage());*/
    }
}
