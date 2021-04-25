package com.w1sh.wave.condition;

import com.w1sh.wave.example.service.impl.BetterCalculatorServiceImpl;
import com.w1sh.wave.example.service.impl.CalculatorServiceImpl;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GenericFilteringConditionalProcessorTest {

    private final Reflections reflections = new Reflections("");
    private final FilteringConditionalProcessor conditionProcessor = new GenericFilteringConditionalProcessor(reflections);

    @Test
    void should_returnClassesFiltered_whenGivenConditionalClasses(){
        final Set<Class<?>> classes = new HashSet<>();
        classes.add(CalculatorServiceImpl.class);
        classes.add(BetterCalculatorServiceImpl.class);

        final Set<Class<?>> filteredClasses = conditionProcessor.processConditionals(classes);

        assertNotNull(filteredClasses);
        assertFalse(filteredClasses.contains(BetterCalculatorServiceImpl.class));
    }
}