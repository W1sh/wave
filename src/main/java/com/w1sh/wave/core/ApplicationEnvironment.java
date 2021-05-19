package com.w1sh.wave.core;

import java.util.List;

public class ApplicationEnvironment extends AbstractApplicationEnvironment {

    public ApplicationEnvironment() { }

    public ApplicationEnvironment(String packagePrefix, boolean overridingEnabled, boolean nullComponentsAllowed,
                                  List<String> activeProfiles) {
        super(packagePrefix, overridingEnabled, nullComponentsAllowed, activeProfiles);
    }

    public static ApplicationEnvironmentBuilder builder() {
        return new ApplicationEnvironmentBuilder();
    }
}
