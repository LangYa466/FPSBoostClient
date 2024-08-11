package com.fpsboost.annotations.system;

import com.fpsboost.module.handlers.ModuleHandle;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Attached {@link ModuleHandle}
 * register Init
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Init {
}
