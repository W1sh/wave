package com.w1sh.wave.core;

public interface Configurable {

    AbstractApplicationEnvironment getEnvironment();

    void setEnvironment(AbstractApplicationEnvironment environment);
}
