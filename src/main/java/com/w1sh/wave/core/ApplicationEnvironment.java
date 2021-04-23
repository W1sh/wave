package com.w1sh.wave.core;

public class ApplicationEnvironment extends AbstractApplicationEnvironment {

    public ApplicationEnvironment() { }

    public ApplicationEnvironment(boolean overridingEnabled, boolean allowNullComponents) {
        super(overridingEnabled, allowNullComponents);
    }

    public static ApplicationEnvironmentBuilder builder() {
        return new ApplicationEnvironmentBuilder();
    }
}
