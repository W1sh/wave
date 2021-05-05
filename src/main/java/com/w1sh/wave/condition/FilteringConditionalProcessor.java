package com.w1sh.wave.condition;

import com.w1sh.wave.core.ContextMetadata;
import com.w1sh.wave.core.Definition;

import java.util.List;

public interface FilteringConditionalProcessor {

    List<Definition> processConditionals(ContextMetadata context);

    ConditionalProcessor getProcessor(Class<?> conditionalAnnotation);

}
