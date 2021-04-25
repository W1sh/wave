package com.w1sh.wave.condition;

import java.util.Set;

public interface FilteringConditionalProcessor {

    Set<Class<?>> processConditionals(Set<Class<?>> classes);

    ConditionalProcessor getProcessor(Class<?> conditionalAnnotation);

}
