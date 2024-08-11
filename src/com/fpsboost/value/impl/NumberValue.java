package com.fpsboost.value.impl;

import com.fpsboost.value.AbstractValue;

public class NumberValue extends AbstractValue<Double> {

    private Double minimum, maximum, increment;

    public NumberValue(String name, Double value, Double minimum, Double maximum, Double increment) {
        super(name);
        this.setValue(value);
        this.minimum = minimum;
        this.maximum = maximum;
        this.increment = increment;
    }

    public NumberValue(String name, Number value, Number minimum, Number maximum, Number increment) {
        super(name);
        this.setValue(value.doubleValue());
        this.minimum = minimum.doubleValue();
        this.maximum = maximum.doubleValue();
        this.increment = increment.doubleValue();
    }

    public NumberValue(String name, Number value, Number minimum, Number maximum, Number increment, boolean canDisplay) {
        super(name);
        this.setValue(value.doubleValue());
        this.minimum = minimum.doubleValue();
        this.maximum = maximum.doubleValue();
        this.increment = increment.doubleValue();
        this.addSupplier(() -> canDisplay);
    }

    public void setMinimum(Double minimum) {
        this.minimum = minimum;
    }

    public void setMaximum(Double maximum) {
        this.maximum = maximum;
    }

    public void setIncrement(Double increment) {
        this.increment = increment;
    }

    public Double getMaximum() {
        return maximum;
    }

    public Double getMinimum() {
        return minimum;
    }

    public Double getIncrement() {
        return increment;
    }
}
