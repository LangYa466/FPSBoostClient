package com.fpsboost.annotations.system;

import com.fpsboost.module.handlers.ModuleHandle;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Attached {@link ModuleHandle}
 * Add this annotation to the method to that this class will be invoked at module disable
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Disable {
}
