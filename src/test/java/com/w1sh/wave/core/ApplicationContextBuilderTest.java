package com.w1sh.wave.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ApplicationContextBuilderTest {

    @Test
    void should_returnApplicationContextInstance_whenBuildIsCalled(){
        final GenericComponentScanner scanner = new GenericComponentScanner();
        final GenericComponentRegistry registry = new GenericComponentRegistry();
        final ApplicationEnvironment environment = new ApplicationEnvironment();
        final ClassDefinitionFactory classDefinitionFactory = new SimpleClassDefinitionFactory();
        final MethodDefinitionFactory methodDefinitionFactory = new SimpleMethodDefinitionFactory();

        ApplicationContext context = ApplicationContext.builder()
                .setRegistry(registry)
                .setScanner(scanner)
                .setEnvironment(environment)
                .setClassDefinitionFactory(classDefinitionFactory)
                .setMethodDefinitionFactory(methodDefinitionFactory)
                .build();

        assertNotNull(context);
        assertEquals(scanner, context.getScanner());
        assertEquals(registry, context.getRegistry());
        assertEquals(environment, context.getEnvironment());
    }
}