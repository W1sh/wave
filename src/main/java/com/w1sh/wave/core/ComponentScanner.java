package com.w1sh.wave.core;

import java.util.Set;

public interface ComponentScanner {

    Set<ComponentDefinition> scan(String... basePackages);
}
