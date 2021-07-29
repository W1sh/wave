package com.w1sh.wave.core;

import com.w1sh.wave.core.annotation.Component;
import com.w1sh.wave.example.service.impl.TestClass;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class QualifiedComponentNameGeneratorTest {

    private final ComponentNameGenerator nameGenerator = new QualifiedComponentNameGenerator();

    @Test
    void should_ReturnFullyQualifiedNameWithNameProvided_WhenGeneratingWithNameDefined(){
        Component component = TestClass.class.getAnnotation(Component.class);
        final String generatedName = nameGenerator.generate(TestClass.class, component);

        assertEquals("com.w1sh.wave.example.service.impl.namedComponent", generatedName);
    }

    @Test
    void should_ReturnFullyQualifiedName_WhenGeneratingWithNoNameDefined(){
        final String generatedName = nameGenerator.generate(getClass(), null);

        assertEquals("com.w1sh.wave.core.QualifiedComponentNameGeneratorTest", generatedName);
    }

}