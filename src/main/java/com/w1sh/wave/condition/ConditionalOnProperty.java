package com.w1sh.wave.condition;

import com.w1sh.wave.core.annotation.Conditional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Conditional
public @interface ConditionalOnProperty {

    String key() default "";

    String value() default "";

    PropertyType type() default PropertyType.SYSTEM;
}
