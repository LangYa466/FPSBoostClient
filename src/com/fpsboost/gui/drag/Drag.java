package com.fpsboost.gui.drag;


public class Drag {

    private float x, y, initialX, initialY;
    private float startX, startY;
    private boolean dragging;

    public Drag(float initialXVal, float initialYVal) {
        this.initialX = initialXVal;
        this.initialY = initialYVal;
        this.x = initialXVal;
        this.y = initialYVal;
    }

    public final void onDraw(int mouseX, int mouseY) {
        if (dragging) {
            x = (mouseX - startX);
            y = (mouseY - startY);
        }
    }

    public final void onClick(int mouseX, int mouseY, int button, boolean canDrag) {
        if (button == 0 && canDrag) {
            dragging = true;
            startX = (int) (mouseX - x);
            startY = (int) (mouseY - y);
        }
    }

    public final void onRelease(int button) {
        if (button == 0) dragging = false;
    }


    public float getInitialX() {
        return initialX;
    }

    public float getInitialY() {
        return initialY;
    }

    public float getStartX() {
        return startX;
    }

    public float getStartY() {
        return startY;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    public void setInitialX(float initialX) {
        this.initialX = initialX;
    }

    public void setInitialY(float initialY) {
        this.initialY = initialY;
    }

    public void setStartX(float startX) {
        this.startX = startX;
    }

    public void setStartY(float startY) {
        this.startY = startY;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }
}
