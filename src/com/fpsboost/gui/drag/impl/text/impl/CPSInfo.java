package com.fpsboost.gui.drag.impl.text.impl;

import com.fpsboost.annotations.event.EventTarget;
import com.fpsboost.annotations.system.Module;
import com.fpsboost.events.render.Render2DEvent;
import com.fpsboost.gui.drag.impl.text.TextDisplay;
import com.fpsboost.module.Category;
import com.fpsboost.util.CPSCounter;
import com.fpsboost.value.impl.BooleanValue;
import com.fpsboost.value.impl.ColorValue;
import com.fpsboost.value.impl.NumberValue;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.ArrayList;


@Module(name = "CPSDisplay",cnName = "CPS显示",description = "显示你的CPS",category = Category.GUI)
public class CPSInfo extends TextDisplay {

    private final BooleanValue backgroundValue = new BooleanValue("背景",true);
    private final ColorValue colorValue = new ColorValue("背景颜色",new Color(0,0,0));
    private final NumberValue opacity = new NumberValue("背景不透明度", 0.25, 0.0, 1, .05);
    private final NumberValue backgroundRadiusValue = new NumberValue("背景圆角值", 2,0,10,1);

    public CPSInfo() {
        super("CPSInfo");
    }

    private final ArrayList<Long> clicksLMB = new ArrayList<>();

    private final ArrayList<Long> clicksRMB = new ArrayList<>();

    private boolean wasPressedLMB;

    private boolean wasPressedRMB;

    @EventTarget
    public void draw(Render2DEvent event) {
        draw(getText(),backgroundValue.getValue(),colorValue.getValue(),opacity.getValue().floatValue(),backgroundRadiusValue.getValue().floatValue());
    }
    public String getText() {

        boolean pressedLMB = Mouse.isButtonDown(0);

        if(pressedLMB != this.wasPressedLMB) {
            long lastPressedLMB = System.currentTimeMillis();
            this.wasPressedLMB = pressedLMB;
            if(pressedLMB) {
                this.clicksLMB.add(lastPressedLMB);
            }
        }

        boolean pressedRMB = Mouse.isButtonDown(1);

        if(pressedRMB != this.wasPressedRMB) {
            long lastPressedRMB = System.currentTimeMillis();
            this.wasPressedRMB = pressedRMB;
            if(pressedRMB) {
                this.clicksRMB.add(lastPressedRMB);
            }
        }

        return String.format("CPS : %s | %s", this.getLMB(), this.getRMB());
    }

    public int getLMB() {
        final long time = System.currentTimeMillis();
        this.clicksLMB.removeIf(aLong -> aLong + 1000 < time);
        return this.clicksLMB.size();
    }

    public int getRMB() {
        final long time = System.currentTimeMillis();
        this.clicksRMB.removeIf(aLong -> aLong + 1000 < time);
        return this.clicksRMB.size();
    }
}
