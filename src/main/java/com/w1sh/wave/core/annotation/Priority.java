package com.w1sh.wave.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines the initialization order for an annotated component.
 * <br>
 * Lower values have higher priority. The default value is {@link Integer#MAX_VALUE}, indicating lowest priority possible.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Priority {

    int value() default Integer.MAX_VALUE;
}
