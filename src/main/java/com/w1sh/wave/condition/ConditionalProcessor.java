package com.w1sh.wave.condition;

import com.w1sh.wave.core.ApplicationContext;

public interface ConditionalProcessor {

    boolean matches(ApplicationContext context, Class<?> conditional);
}
