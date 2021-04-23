package com.w1sh.wave.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApplicationEnvironmentBuilderTest {

    @Test
    void should_returnApplicationEnvironmentInstance_whenBuildIsCalled(){
        ApplicationEnvironment environment = ApplicationEnvironment.builder()
                .setAllowNullComponents(true)
                .setOverridingEnabled(true)
                .build();

        assertNotNull(environment);
        assertTrue(environment.isNullComponentsAllowed());
        assertTrue(environment.isOverridingEnabled());
    }
}