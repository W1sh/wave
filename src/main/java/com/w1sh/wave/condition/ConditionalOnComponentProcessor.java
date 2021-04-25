package com.w1sh.wave.condition;

import java.util.List;
import java.util.Set;

@Processor(ConditionalOnComponent.class)
public class ConditionalOnComponentProcessor implements ConditionalProcessor {

    @Override
    public boolean matches(Set<Class<?>> classes, Class<?> conditional) {
        ConditionalOnComponent onComponent = conditional.getAnnotation(ConditionalOnComponent.class);
        return classes.containsAll(List.of(onComponent.value()));
    }

}
