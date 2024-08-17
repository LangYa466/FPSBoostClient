package com.fpsboost.gui.ui.components;

import com.fpsboost.Access;

public abstract class Component implements Access.InstanceAccess {

    public float x;
    public float y;
    public float bigRecty;
    public boolean hovering;
    public float rectWidth;

    public abstract void initGui();

    public abstract void keyTyped(char typedChar, int keyCode);

    public abstract void drawScreen(int mouseX, int mouseY);

    public abstract void mouseClicked(int mouseX, int mouseY, int button);

    public abstract void mouseReleased(int mouseX, int mouseY, int button);
}