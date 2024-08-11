package com.fpsboost.events.movement;

import com.fpsboost.events.base.Event;

public class MoveEvent implements Event {

    public double x;
    public double y;
    public double z;

    public MoveEvent(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

}
