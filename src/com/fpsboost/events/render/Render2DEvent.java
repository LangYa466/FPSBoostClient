package com.fpsboost.events.render;

import net.minecraft.client.gui.ScaledResolution;
import com.fpsboost.events.base.Event;

public class Render2DEvent implements Event {

    private final ScaledResolution scaledResolution;
    private final float renderPartialTicks;
    private boolean pre;

    public Render2DEvent(ScaledResolution scaledResolution, float renderPartialTicks, boolean isPre) {
        this.scaledResolution = scaledResolution;
        this.renderPartialTicks = renderPartialTicks;
        this.pre = isPre;
    }

    public boolean isPre() {
        return pre;
    }

    public boolean isPost() {
        return !pre;
    }

    public ScaledResolution getScaledResolution() {
        return scaledResolution;
    }

    public float getRenderPartialTicks() {
        return renderPartialTicks;
    }
}
