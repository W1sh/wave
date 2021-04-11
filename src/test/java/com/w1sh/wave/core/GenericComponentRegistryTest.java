package com.w1sh.wave.core;

import com.w1sh.wave.example.service.impl.CalculatorServiceImpl;
import com.w1sh.wave.example.service.impl.LazyServiceImpl;
import org.junit.jupiter.api.Test;

class GenericComponentRegistryTest {

    private final ComponentRegistry registry = new GenericComponentRegistry();

    @Test
    void test(){
        // TODO: complete
        registry.register(LazyServiceImpl.class);
        registry.register(CalculatorServiceImpl.class);
    }
}