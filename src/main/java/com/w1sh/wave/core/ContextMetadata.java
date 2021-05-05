package com.w1sh.wave.core;

import java.util.List;

public class ContextMetadata {

    private final ApplicationContext context;
    private final List<Definition> conditionalDefinitions;
    private final AbstractApplicationEnvironment environment;

    public ContextMetadata(ApplicationContext context, List<Definition> conditionalDefinitions, AbstractApplicationEnvironment environment) {
        this.context = context;
        this.conditionalDefinitions = conditionalDefinitions;
        this.environment = environment;
    }

    public ApplicationContext getContext() {
        return context;
    }

    public List<Definition> getConditionalDefinitions() {
        return conditionalDefinitions;
    }

    public AbstractApplicationEnvironment getEnvironment() {
        return environment;
    }
}
