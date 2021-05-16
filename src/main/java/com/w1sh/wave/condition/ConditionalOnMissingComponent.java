package com.w1sh.wave.condition;

import com.w1sh.wave.core.annotation.Conditional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Conditional
public @interface ConditionalOnMissingComponent {

    Class<?>[] value() default {};

    String[] names() default {};
}
