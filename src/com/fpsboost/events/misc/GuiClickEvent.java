package com.fpsboost.events.misc;

import com.fpsboost.events.base.Event;

public class GuiClickEvent extends Event.EventCancellable {
    private final int mouseX, mouseY, mouseButton;

    public GuiClickEvent(int mouseX, int mouseY, int mouseButton) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.mouseButton = mouseButton;
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public int getMouseButton() {
        return mouseButton;
    }
}