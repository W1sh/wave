package com.w1sh.wave.core;

import java.util.List;

public class ApplicationEnvironmentBuilder {

    private String packagePrefix = "";
    private List<String> activeProfiles;
    private boolean overridingEnabled;
    private boolean allowNullComponents;

    public ApplicationEnvironmentBuilder setPackagePrefix(String packagePrefix) {
        this.packagePrefix = packagePrefix;
        return this;
    }

    public ApplicationEnvironmentBuilder setOverridingEnabled(boolean overridingEnabled) {
        this.overridingEnabled = overridingEnabled;
        return this;
    }

    public ApplicationEnvironmentBuilder setAllowNullComponents(boolean allowNullComponents) {
        this.allowNullComponents = allowNullComponents;
        return this;
    }

    public ApplicationEnvironmentBuilder setActiveProfiles(String... activeProfiles) {
        this.activeProfiles = List.of(activeProfiles);
        return this;
    }

    public ApplicationEnvironment build() {
        return new ApplicationEnvironment(packagePrefix, overridingEnabled, allowNullComponents, activeProfiles);
    }

    public String getPackagePrefix() {
        return packagePrefix;
    }

    public List<String> getActiveProfiles() {
        return activeProfiles;
    }

    public boolean isOverridingEnabled() {
        return overridingEnabled;
    }

    public boolean isAllowNullComponents() {
        return allowNullComponents;
    }
}
