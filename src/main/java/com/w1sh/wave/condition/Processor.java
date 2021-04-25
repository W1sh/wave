package com.w1sh.wave.condition;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Processor {

    Class<? extends Annotation> value();
}
