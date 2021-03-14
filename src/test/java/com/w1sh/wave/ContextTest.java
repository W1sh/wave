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
    void should_ReturnNull_WhenNoComponentIsPresentInContextForGivenClass(){
        final BetterCalculatorServiceImpl component = Context.getComponent(
                BetterCalculatorServiceImpl.class);

        Assertions.assertNull(component);
    }

    @Test
    void should_ReturnNull_WhenNoComponentIsPresentInContextForGivenClassAndName(){
        final BetterCalculatorServiceImpl component = Context.getComponent(
                BetterCalculatorServiceImpl.class, "betterCalculatorService");

        Assertions.assertNull(component);
    }
}
