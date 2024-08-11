package com.fpsboost.events.misc;

import net.minecraft.util.MovingObjectPosition;
import com.fpsboost.events.base.Event;

public class MiddleClickEvent implements Event {
    private final MovingObjectPosition objectPosition;

    public MiddleClickEvent(MovingObjectPosition mouseOver) {
        this.objectPosition = mouseOver;
    }

    public MovingObjectPosition getObjectPosition() {
        return objectPosition;
    }
}
