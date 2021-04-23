package com.w1sh.wave.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ApplicationContextBuilderTest {

    @Test
    void should_returnApplicationContextInstance_whenBuildIsCalled(){
        final GenericComponentScanner scanner = new GenericComponentScanner("");
        final GenericComponentRegistry registry = new GenericComponentRegistry();
        final ApplicationEnvironment environment = new ApplicationEnvironment();

        ApplicationContext context = ApplicationContext.builder()
                .setRegistry(registry)
                .setScanner(scanner)
                .setEnvironment(environment)
                .build();

        Assertions.assertNotNull(context);
        Assertions.assertEquals(scanner, context.getScanner());
        Assertions.assertEquals(registry, context.getRegistry());
        Assertions.assertEquals(environment, context.getEnvironment());
    }
}