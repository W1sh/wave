package com.w1sh.wave.condition;

import com.w1sh.wave.example.service.impl.BetterCalculatorServiceImpl;
import com.w1sh.wave.example.service.impl.CalculatorServiceImpl;
import com.w1sh.wave.example.service.impl.LazyServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConditionalOnMissingComponentProcessorTest {

    private final ConditionalOnMissingComponentProcessor processor = new ConditionalOnMissingComponentProcessor();

    @Test
    void should_returnTrue_WhenGivenConditionalValuesAreNotPresentInContext() {
        final Set<Class<?>> classes = Set.of(LazyServiceImpl.class);

        final boolean matches = processor.matches(classes, BetterCalculatorServiceImpl.class);

        assertTrue(matches);
    }

    @Test
    void should_returnFalse_WhenGivenConditionalValuesArePresentInContext() {
        final Set<Class<?>> classes = Set.of(CalculatorServiceImpl.class);

        final boolean matches = processor.matches(classes, BetterCalculatorServiceImpl.class);

        assertFalse(matches);
    }
}