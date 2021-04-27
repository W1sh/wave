package com.w1sh.wave.core;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GenericComponentScannerTest {

    private final ComponentScanner scanner = new GenericComponentScanner("com.w1sh.wave");

    @Test
    void should_returnSetOfAnnotatedClasses_WhenSearchingInGivenPackage(){
        final Set<Class<?>> classes = scanner.scanClasses();

        assertNotNull(classes);
        assertFalse(classes.isEmpty());
    }

}