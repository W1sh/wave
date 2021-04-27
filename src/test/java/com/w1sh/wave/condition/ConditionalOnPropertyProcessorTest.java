package com.w1sh.wave.condition;

import com.w1sh.wave.example.service.impl.TestConfiguration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConditionalOnPropertyProcessorTest {

    private final ConditionalOnPropertyProcessor processor = new ConditionalOnPropertyProcessor();

    @Test
    void should_returnTrue_WhenGivenConditionalKeyAndValueArePresentInSystemProperties() {
        System.setProperty("test", "true");
        final boolean matches = processor.matches(null, TestConfiguration.class);

        assertTrue(matches);
    }

    @Test
    void should_returnTrue_WhenGivenConditionalKeyAndValueAreNotPresentInSystemProperties() {
        final boolean matches = processor.matches(null, TestConfiguration.class);

        assertFalse(matches);
    }

}