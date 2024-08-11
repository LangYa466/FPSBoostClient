package com.fpsboost.value.impl;

import com.fpsboost.value.AbstractValue;

public class BooleanValue extends AbstractValue<Boolean> {

    public BooleanValue(String name, boolean enabled) {
        super(name);
        this.setValue(enabled);
    }

}
