package com.fpsboost.gui.drag;

import com.fpsboost.module.handlers.ModuleHandle;
import com.fpsboost.util.HoveringUtil;
import com.fpsboost.util.animations.Animation;
import com.fpsboost.util.animations.Direction;
import com.fpsboost.util.animations.impl.DecelerateAnimation;
import com.fpsboost.util.render.ColorUtil;
import com.fpsboost.util.render.RoundedUtil;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.awt.*;

public class Dragging {
    @Expose
    @SerializedName("x")
    private float xPos;
    @Expose
    @SerializedName("y")
    private float yPos;

    public float initialXVal;
    public float initialYVal;

    private float startX, startY;
    private boolean dragging;

    private float width, height;

    @Expose
    @SerializedName("name")
    private String name;

    private final Class<?> module;

    public Animation hoverAnimation = new DecelerateAnimation(250, 1, Direction.BACKWARDS);

    public Dragging(Class<?> module, String name, float initialXVal, float initialYVal) {
        this.module = module;
        this.name = name;
        this.xPos = initialXVal;
        this.yPos = initialYVal;
        this.initialXVal = initialXVal;
        this.initialYVal = initialYVal;
    }

    public Class<?> getModule() {
        return module;
    }

    public String getName() {
        return name;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getX() {
        return xPos;
    }

    public void setX(float x) {
        this.xPos = x;
    }

    public float getY() {
        return yPos;
    }

    public void setY(float y) {
        this.yPos = y;
    }


    private String longestModule;

    public final void onDraw(int mouseX, int mouseY) {
        boolean hovering = HoveringUtil.isHovering(xPos, yPos, width, height, mouseX, mouseY);
        if (dragging) {
            xPos = mouseX - startX;
            yPos = mouseY - startY;
        }
        hoverAnimation.setDirection(hovering ? Direction.FORWARDS : Direction.BACKWARDS);
        if (!hoverAnimation.isDone() || hoverAnimation.finished(Direction.FORWARDS)) {
            RoundedUtil.drawRoundOutline(xPos - 4, yPos - 4, width + 8, height + 8, 10, 2,
                    ColorUtil.applyOpacity(Color.WHITE, 0), ColorUtil.applyOpacity(Color.WHITE, (float) hoverAnimation.getOutput().floatValue()));
        }
    }

    public final void onClick(int mouseX, int mouseY, int button) {
        boolean canDrag = HoveringUtil.isHovering(xPos, yPos, width, height, mouseX, mouseY);
        if (button == 0 && canDrag) {
            dragging = true;
            startX = (int) (mouseX - xPos);
            startY = (int) (mouseY - yPos);
        }
    }
    public final void onRelease(int button) {
        if (button == 0) dragging = false;
    }

    public void setXPos(float x) {
        this.xPos = x;
    }

    public void setYPos(float y) {
        this.yPos = y;
    }

    public float getXPos() {
        return xPos;
    }

    public float getYPos() {
        return yPos;
    }

    public void setWH(float width, float height) {
        this.width = width;
        this.height = height;
    }
}