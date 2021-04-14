package com.w1sh.wave.core;

public abstract class AbstractApplicationEnvironment {

    private String prefix;
    private boolean overridingEnabled;

    public boolean isOverridingEnabled() {
        return overridingEnabled;
    }

    public void setOverridingEnabled(boolean overridingEnabled) {
        this.overridingEnabled = overridingEnabled;
    }
}
