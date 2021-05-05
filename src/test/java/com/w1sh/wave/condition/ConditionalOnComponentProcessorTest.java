package com.w1sh.wave.condition;

import com.w1sh.wave.core.ApplicationContext;
import com.w1sh.wave.core.ContextMetadata;
import com.w1sh.wave.example.service.impl.CalculatorServiceImpl;
import com.w1sh.wave.example.service.impl.DuplicateCalculatorServiceImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class ConditionalOnComponentProcessorTest {

    private final ApplicationContext context = mock(ApplicationContext.class);
    private final ContextMetadata contextMetadata = new ContextMetadata(context, null, null);
    private final ConditionalOnComponentProcessor processor = new ConditionalOnComponentProcessor();

    @Test
    void should_returnTrue_WhenGivenConditionalValuesArePresentInContext() {
        when(context.containsComponent(CalculatorServiceImpl.class)).thenReturn(true);

        final boolean matches = processor.matches(contextMetadata, DuplicateCalculatorServiceImpl.class);

        verify(context, times(1)).containsComponent(any(Class.class));
        assertTrue(matches);
    }

    @Test
    void should_returnFalse_WhenGivenConditionalValuesAreNotPresentInContext() {
        when(context.containsComponent(CalculatorServiceImpl.class)).thenReturn(false);

        final boolean matches = processor.matches(contextMetadata, DuplicateCalculatorServiceImpl.class);

        verify(context, times(1)).containsComponent(any(Class.class));
        assertFalse(matches);
    }
}