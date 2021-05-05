package com.w1sh.wave.core;

import java.util.List;

public abstract class AbstractApplicationEnvironment {

    private String packagePrefix;
    private List<String> activeProfiles;
    private boolean overridingEnabled;
    private boolean nullComponentsAllowed;

    public AbstractApplicationEnvironment() { }

    public AbstractApplicationEnvironment(String packagePrefix, boolean overridingEnabled, boolean nullComponentsAllowed,
                                          List<String> activeProfiles) {
        this.packagePrefix = packagePrefix;
        this.overridingEnabled = overridingEnabled;
        this.nullComponentsAllowed = nullComponentsAllowed;
        this.activeProfiles = activeProfiles;
    }

    public String getPackagePrefix() {
        return packagePrefix;
    }

    public void setPackagePrefix(String packagePrefix) {
        this.packagePrefix = packagePrefix;
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

    public List<String> getActiveProfiles() {
        return activeProfiles;
    }

    public void setActiveProfiles(List<String> activeProfiles) {
        this.activeProfiles = activeProfiles;
    }
}
