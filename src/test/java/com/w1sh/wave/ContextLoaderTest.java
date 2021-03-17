package com.w1sh.wave;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class ContextLoaderTest {

    private ContextLoader contextLoader;
    private Injector injector;

    @BeforeEach
    void setUp() {
        injector = mock(Injector.class);
        contextLoader = new ContextLoader(injector, "com.w1sh.wave");
        contextLoader.loadClassAnnotatedWithComponent();
        Context.clearContext();
    }

    @Test
    void should_LoadAllClasses_WhenClassesAreAnnotatedWithComponentAndHaveDesiredPrefix(){
        contextLoader.loadClassAnnotatedWithComponent();

        verify(injector, times(6)).inject(any());
    }
}
