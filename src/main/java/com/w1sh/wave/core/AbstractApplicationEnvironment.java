package com.w1sh.wave.core;

public abstract class AbstractApplicationEnvironment {

    private String prefix;
    private boolean overridingEnabled;
    private boolean allowNullComponents;

    public boolean isOverridingEnabled() {
        return overridingEnabled;
    }

    public void setOverridingEnabled(boolean overridingEnabled) {
        this.overridingEnabled = overridingEnabled;
    }

    public boolean isAllowNullComponents() {
        return allowNullComponents;
    }

    public void setAllowNullComponents(boolean allowNullComponents) {
        this.allowNullComponents = allowNullComponents;
    }
}
