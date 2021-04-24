package com.w1sh.wave.core;

import com.w1sh.wave.example.service.impl.TestConfiguration;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GenericComponentScannerTest {

    private final ComponentScanner scanner = new GenericComponentScanner("com.w1sh.wave");

    @Test
    void should_returnSetOfAnnotatedClasses_WhenSearchingInGivenPackage(){
        final Set<Class<?>> classes = scanner.scan();

        assertNotNull(classes);
        assertFalse(classes.isEmpty());
    }

    @Test
    void should_returnConfigurationClasses_WhenSearchingInGivenPackage(){
        final Set<Class<?>> classes = scanner.scan();

        assertNotNull(classes);
        assertFalse(classes.isEmpty());
        assertTrue(classes.contains(TestConfiguration.class));
    }
}