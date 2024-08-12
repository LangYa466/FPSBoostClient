package com.fpsboost.gui.drag.impl;

import com.fpsboost.annotations.event.EventTarget;
import com.fpsboost.annotations.system.Module;
import com.fpsboost.events.render.Render2DEvent;
import com.fpsboost.module.Category;
import com.fpsboost.util.CPSCounter;
import com.fpsboost.value.impl.BooleanValue;
import com.fpsboost.value.impl.NumberValue;


@Module(value = "CPS显示",category = Category.GUI)
public class CPSInfo extends TextDisplay {

    private final BooleanValue backgroundValue = new BooleanValue("背景",true);
    private final NumberValue opacity = new NumberValue("背景不透明度", 0.25, 0.0, 1, .05);
    private final NumberValue backgroundRadiusValue = new NumberValue("背景圆角值", 2,0,10,1);

    public CPSInfo() {
        super("CPSInfo");
    }


    @EventTarget
    public void draw(Render2DEvent event) {
        String text = String.format("CPS : %s | %s", CPSCounter.getCPS(CPSCounter.MouseButton.LEFT), CPSCounter.getCPS(CPSCounter.MouseButton.RIGHT));
        draw(text,backgroundValue.getValue(),opacity.getValue().floatValue(),backgroundRadiusValue.getValue().floatValue());
    }
}
