package com.w1sh.wave.condition;

import java.util.Set;

public interface ConditionalProcessor {

    boolean matches(Set<Class<?>> classes, Class<?> conditional);
}
