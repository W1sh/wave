package com.w1sh.wave;

import com.w1sh.wave.example.service.CalculatorService;
import com.w1sh.wave.example.service.impl.BetterCalculatorServiceImpl;
import com.w1sh.wave.example.service.impl.CalculatorServiceImpl;
import com.w1sh.wave.example.service.impl.DuplicateCalculatorServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContextTest {

    @BeforeAll
    static void beforeAll() {
        Context.initialize();
    }

    @BeforeEach
    void setUp() {
        Context.clearContext();
    }

    @Test
    void should_ThrowException_WhenNoComponentIsPresentInContextForGivenClass(){
        String message = "No injection candidate found";
        ComponentCreationException exception = Assertions.assertThrows(ComponentCreationException.class, () -> {
            Context.getComponent(BetterCalculatorServiceImpl.class);
        });

        Assertions.assertNotNull(exception);
        Assertions.assertNotNull(exception.getMessage());
        Assertions.assertTrue(exception.getMessage().contains(message));
    }

    @Test
    void should_ThrowException_WhenMultipleCandidatesArePresentInContextForGivenClass(){
        String message = "Multiple injection candidates found";
        CalculatorServiceImpl calculatorService = new CalculatorServiceImpl();
        BetterCalculatorServiceImpl betterCalculatorService = new BetterCalculatorServiceImpl();
        Context.addComponent(CalculatorServiceImpl.class, calculatorService);
        Context.addComponent(BetterCalculatorServiceImpl.class, betterCalculatorService);

        ComponentCreationException exception = Assertions.assertThrows(ComponentCreationException.class, () -> {
            Context.getComponent(CalculatorService.class);
        });

        Assertions.assertNotNull(exception);
        Assertions.assertNotNull(exception.getMessage());
        Assertions.assertTrue(exception.getMessage().contains(message));
    }

    @Test
    void should_ThrowException_WhenNoComponentIsPresentInContextForGivenClassAndName(){
        String message = "No injection candidate found";
        ComponentCreationException exception = Assertions.assertThrows(ComponentCreationException.class, () -> {
            Context.getComponent(BetterCalculatorServiceImpl.class, "betterCalculatorService");
        });

        Assertions.assertNotNull(exception);
        Assertions.assertNotNull(exception.getMessage());
        Assertions.assertTrue(exception.getMessage().contains(message));
    }

    @Test
    void should_ThrowException_WhenMultipleCandidatesArePresentInContextForGivenClassAndName(){
        String message = "Multiple injection candidates found";
        CalculatorServiceImpl calculatorService = new CalculatorServiceImpl();
        DuplicateCalculatorServiceImpl duplicateCalculatorService = new DuplicateCalculatorServiceImpl();
        Context.addComponent(CalculatorServiceImpl.class, calculatorService);
        Context.addComponent(DuplicateCalculatorServiceImpl.class, duplicateCalculatorService);

        ComponentCreationException exception = Assertions.assertThrows(ComponentCreationException.class, () -> {
            Context.getComponent(CalculatorService.class);
        });

        Assertions.assertNotNull(exception);
        Assertions.assertNotNull(exception.getMessage());
        Assertions.assertTrue(exception.getMessage().contains(message));
    }
}
