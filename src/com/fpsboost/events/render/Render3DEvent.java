package com.fpsboost.events.render;

import com.fpsboost.events.base.Event;

public class Render3DEvent implements Event {

    private final float renderPartialTicks;

    public Render3DEvent(float renderPartialTicks) {
        this.renderPartialTicks = renderPartialTicks;
    }

    public float getRenderPartialTicks() {
        return renderPartialTicks;
    }
}
