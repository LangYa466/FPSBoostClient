package com.fpsboost.gui.clickGui.book;

import com.fpsboost.util.animations.Animation;
import com.fpsboost.util.animations.Direction;
import com.fpsboost.util.animations.impl.SmoothStepAnimation;
import org.lwjgl.input.Mouse;

/**
 * @author cedo
 * @author Foggy
 */
public class Scroll {


    private float maxScroll = Float.MAX_VALUE, minScroll = 0, rawScroll = 0;
    private float scroll;
    private Animation scrollAnimation = new SmoothStepAnimation(0, 0, Direction.BACKWARDS);

    public void onScroll(int ms) {
        scroll = rawScroll - scrollAnimation.getOutput().floatValue();
        rawScroll += Mouse.getDWheel() / 4f;
        rawScroll = Math.max(Math.min(minScroll, rawScroll), -maxScroll);
        scrollAnimation = new SmoothStepAnimation(ms, rawScroll - scroll, Direction.BACKWARDS);
    }

    public boolean isScrollAnimationDone() {
        return scrollAnimation.isDone();
    }

    public float getScroll() {
        scroll = rawScroll - scrollAnimation.getOutput().floatValue();
        return scroll;
    }

    public Animation getScrollAnimation() {
        return scrollAnimation;
    }

    public float getMaxScroll() {
        return maxScroll;
    }

    public void setMaxScroll(float maxScroll) {
        this.maxScroll = maxScroll;
    }
}