package com.w1sh.wave.condition;

import com.w1sh.wave.core.*;
import com.w1sh.wave.core.annotation.Conditional;
import com.w1sh.wave.core.exception.UnresolvableConditionalException;
import com.w1sh.wave.example.service.impl.BetterCalculatorServiceImpl;
import com.w1sh.wave.example.service.impl.CalculatorServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SimpleFilteringConditionalProcessorTest {

    private final ApplicationContext context = Mockito.mock(ApplicationContext.class);
    private final ApplicationEnvironment environment = Mockito.mock(ApplicationEnvironment.class);
    private final Reflections reflections = new Reflections("");
    private final FilteringConditionalProcessor conditionProcessor = new SimpleFilteringConditionalProcessor(reflections);

    private static List<Definition> conditionalDefinitions = new ArrayList<>();

    @BeforeAll
    static void beforeAll() {
        Definition definition = new ComponentDefinition(BetterCalculatorServiceImpl.class);
        definition.setConditional(true);
        conditionalDefinitions.add(definition);
    }

    @Test
    void should_returnClassesFiltered_whenGivenConditionalClasses(){
        ContextMetadata contextMetadata = new ContextMetadata(context, conditionalDefinitions, environment);
        when(context.containsComponent(CalculatorServiceImpl.class)).thenReturn(false);

        final List<Definition> passedConditionalDefinitions = conditionProcessor.processConditionals(contextMetadata);

        verify(context, times(1)).containsComponent(any(Class.class));
        assertNotNull(passedConditionalDefinitions);
        assertFalse(passedConditionalDefinitions.isEmpty());
    }

    @Test
    void should_throwException_whenProcessorDoesNotExistForGivenAnnotation(){
        assertThrows(UnresolvableConditionalException.class, () -> conditionProcessor.getProcessor(Conditional.class));
    }
}