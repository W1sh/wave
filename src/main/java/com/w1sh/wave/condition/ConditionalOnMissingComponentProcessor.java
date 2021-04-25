package com.w1sh.wave.condition;

import java.util.List;
import java.util.Set;

@Processor(ConditionalOnMissingComponent.class)
public class ConditionalOnMissingComponentProcessor implements ConditionalProcessor {

    @Override
    public boolean matches(Set<Class<?>> classes, Class<?> conditional) {
        ConditionalOnMissingComponent onMissingComponent = conditional.getAnnotation(ConditionalOnMissingComponent.class);
        return !classes.containsAll(List.of(onMissingComponent.value()));
    }
}
