package com.w1sh.wave.condition;

import com.w1sh.wave.core.ApplicationContext;

import java.util.List;

@Processor(ConditionalOnComponent.class)
public class ConditionalOnComponentProcessor implements ConditionalProcessor {

    @Override
    public boolean matches(ApplicationContext context, Class<?> conditional) {
        ConditionalOnComponent onComponent = conditional.getAnnotation(ConditionalOnComponent.class);
        return List.of(onComponent.value()).stream().allMatch(context::containsComponent) &&
                List.of(onComponent.names()).stream().allMatch(context::containsComponent);
    }

}
