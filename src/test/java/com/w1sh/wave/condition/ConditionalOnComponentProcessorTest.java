package com.w1sh.wave.condition;

import com.w1sh.wave.example.service.impl.CalculatorServiceImpl;
import com.w1sh.wave.example.service.impl.DuplicateCalculatorServiceImpl;
import com.w1sh.wave.example.service.impl.LazyServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConditionalOnComponentProcessorTest {

    private final ConditionalOnComponentProcessor processor = new ConditionalOnComponentProcessor();

    @Test
    void should_returnTrue_WhenGivenConditionalValuesArePresentInContext() {
        final Set<Class<?>> classes = Set.of(CalculatorServiceImpl.class);

        final boolean matches = processor.matches(classes, DuplicateCalculatorServiceImpl.class);

        assertTrue(matches);
    }

    @Test
    void should_returnFalse_WhenGivenConditionalValuesAreNotPresentInContext() {
        final Set<Class<?>> classes = Set.of(LazyServiceImpl.class);

        final boolean matches = processor.matches(classes, DuplicateCalculatorServiceImpl.class);

        assertFalse(matches);
    }
}