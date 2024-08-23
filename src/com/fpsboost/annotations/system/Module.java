package com.fpsboost.annotations.system;

import com.fpsboost.module.Category;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Module {
    String name();
    String cnName() default "";
    String description() default "";
    Category category();
}
