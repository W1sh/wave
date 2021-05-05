package com.w1sh.wave.condition;

import com.w1sh.wave.core.ContextMetadata;

import java.util.List;

@Processor(ConditionalOnMissingComponent.class)
public class ConditionalOnMissingComponentProcessor implements ConditionalProcessor {

    @Override
    public boolean matches(ContextMetadata context, Class<?> conditional) {
        ConditionalOnMissingComponent onMissingComponent = conditional.getAnnotation(ConditionalOnMissingComponent.class);
        return List.of(onMissingComponent.value()).stream().noneMatch(context.getContext()::containsComponent);
    }
}
