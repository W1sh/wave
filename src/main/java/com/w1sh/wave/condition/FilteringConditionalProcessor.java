package com.w1sh.wave.condition;

import com.w1sh.wave.core.ApplicationContext;
import com.w1sh.wave.core.Definition;

public interface FilteringConditionalProcessor {

    boolean evaluate(ApplicationContext context, Definition definitionToProcess);

    ConditionalProcessor getProcessor(Class<?> conditionalAnnotation);

}
