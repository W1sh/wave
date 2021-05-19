package com.w1sh.wave.condition;

import com.w1sh.wave.core.ApplicationContext;

import java.util.List;

@Processor(ConditionalOnMissingComponent.class)
public class ConditionalOnMissingComponentProcessor implements ConditionalProcessor {

    @Override
    public boolean matches(ApplicationContext context, Class<?> conditional) {
        ConditionalOnMissingComponent onMissingComponent = conditional.getAnnotation(ConditionalOnMissingComponent.class);
        return List.of(onMissingComponent.value()).stream().noneMatch(context::containsComponent) &&
                List.of(onMissingComponent.names()).stream().noneMatch(context::containsComponent);
    }
}
