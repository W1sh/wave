package com.w1sh.wave.core;

public abstract class AbstractApplicationEnvironment {

    private boolean overridingEnabled;
    private boolean nullComponentsAllowed;

    public AbstractApplicationEnvironment() { }

    public AbstractApplicationEnvironment(boolean overridingEnabled, boolean nullComponentsAllowed) {
        this.overridingEnabled = overridingEnabled;
        this.nullComponentsAllowed = nullComponentsAllowed;
    }

    public boolean isOverridingEnabled() {
        return overridingEnabled;
    }

    public void setOverridingEnabled(boolean overridingEnabled) {
        this.overridingEnabled = overridingEnabled;
    }

    public boolean isNullComponentsAllowed() {
        return nullComponentsAllowed;
    }

    public void setNullComponentsAllowed(boolean nullComponentsAllowed) {
        this.nullComponentsAllowed = nullComponentsAllowed;
    }
}
