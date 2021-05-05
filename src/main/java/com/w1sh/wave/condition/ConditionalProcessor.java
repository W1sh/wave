package com.w1sh.wave.condition;

import com.w1sh.wave.core.ContextMetadata;

public interface ConditionalProcessor {

    boolean matches(ContextMetadata context, Class<?> conditional);
}
