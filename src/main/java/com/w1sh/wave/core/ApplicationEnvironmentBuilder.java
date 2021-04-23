package com.w1sh.wave.core;

public class ApplicationEnvironmentBuilder {

    private boolean overridingEnabled;
    private boolean allowNullComponents;

    public ApplicationEnvironmentBuilder setOverridingEnabled(boolean overridingEnabled) {
        this.overridingEnabled = overridingEnabled;
        return this;
    }

    public ApplicationEnvironmentBuilder setAllowNullComponents(boolean allowNullComponents) {
        this.allowNullComponents = allowNullComponents;
        return this;
    }

    public ApplicationEnvironment build() {
        return new ApplicationEnvironment(overridingEnabled, allowNullComponents);
    }

    public boolean isOverridingEnabled() {
        return overridingEnabled;
    }

    public boolean isAllowNullComponents() {
        return allowNullComponents;
    }
}
