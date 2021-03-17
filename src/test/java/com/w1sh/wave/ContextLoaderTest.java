package com.w1sh.wave;

import com.w1sh.wave.example.service.impl.DuplicateCalculatorServiceImpl;
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
        Context.clearContext();
    }

    @Test
    void should_LoadAllClasses_WhenClassesAreAnnotatedWithComponentAndHaveDesiredPrefix(){
        contextLoader.loadClassAnnotatedWithComponent();

        verify(injector, times(2)).inject(any());
    }

    @Test
    void should_NotInitializeLazyComponents_WhenClassesAreAnnotatedWithComponentAndAreDefinedLazy(){
        contextLoader.loadClassAnnotatedWithComponent();

        verify(injector, never()).inject(DuplicateCalculatorServiceImpl.class);
    }
}
