package com.w1sh.wave.condition;

import com.w1sh.wave.core.ApplicationContext;
import com.w1sh.wave.core.ComponentDefinition;
import com.w1sh.wave.core.Definition;
import com.w1sh.wave.core.annotation.Conditional;
import com.w1sh.wave.core.exception.UnresolvableConditionalException;
import com.w1sh.wave.example.service.impl.BetterCalculatorServiceImpl;
import com.w1sh.wave.example.service.impl.CalculatorServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.reflections.Reflections;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SimpleFilteringConditionalProcessorTest {

    private final ApplicationContext context = Mockito.mock(ApplicationContext.class);
    private final Reflections reflections = new Reflections("");
    private final FilteringConditionalProcessor conditionProcessor = new SimpleFilteringConditionalProcessor(reflections);

    @Test
    void should_returnClassesFiltered_whenGivenConditionalClasses(){
        Definition definition = new ComponentDefinition(BetterCalculatorServiceImpl.class);
        definition.setConditional(true);
        when(context.containsComponent(CalculatorServiceImpl.class)).thenReturn(false);

        boolean passed = conditionProcessor.evaluate(context, definition);

        verify(context, times(1)).containsComponent(any(Class.class));
        assertTrue(passed);
    }

    @Test
    void should_throwException_whenProcessorDoesNotExistForGivenAnnotation(){
        assertThrows(UnresolvableConditionalException.class, () -> conditionProcessor.getProcessor(Conditional.class));
    }
}