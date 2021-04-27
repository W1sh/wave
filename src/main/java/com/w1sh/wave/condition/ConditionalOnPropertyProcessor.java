package com.w1sh.wave.condition;

import java.util.Set;

@Processor(ConditionalOnProperty.class)
public class ConditionalOnPropertyProcessor implements ConditionalProcessor {

    @Override
    public boolean matches(Set<Class<?>> classes, Class<?> conditional) {
        ConditionalOnProperty onProperty = conditional.getAnnotation(ConditionalOnProperty.class);
        return switch (onProperty.type()) {
            case SYSTEM -> matchesSystemProperty(onProperty);
            case ENVIRONMENT -> matchesEnvironmentProperty(onProperty);
        };
    }

    private boolean matchesSystemProperty(ConditionalOnProperty conditional) {
        final String property = System.getProperty(conditional.key());
        return property != null && property.equalsIgnoreCase(conditional.value());
    }

    private boolean matchesEnvironmentProperty(ConditionalOnProperty conditional) {
        final String property = System.getenv(conditional.key());
        return property != null && property.equalsIgnoreCase(conditional.value());
    }
}
