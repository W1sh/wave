package com.w1sh.wave.condition;

import java.util.Set;

public interface FilteringConditionProcessor {

    Set<Class<?>> processConditionals(Set<Class<?>> classes);

}
