package com.w1sh.wave.core;

import java.util.List;

public class ApplicationEnvironment extends AbstractApplicationEnvironment {

    public ApplicationEnvironment() { }

    public ApplicationEnvironment(boolean overridingEnabled, boolean allowNullComponents, List<String> activeProfiles) {
        super(overridingEnabled, allowNullComponents, activeProfiles);
    }

    public static ApplicationEnvironmentBuilder builder() {
        return new ApplicationEnvironmentBuilder();
    }
}
