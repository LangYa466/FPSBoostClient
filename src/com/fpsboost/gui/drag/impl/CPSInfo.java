package com.fpsboost.gui.drag.impl;

import com.fpsboost.Access;
import com.fpsboost.annotations.event.EventTarget;
import com.fpsboost.events.render.Render2DEvent;
import com.fpsboost.gui.drag.Dragging;
import com.fpsboost.gui.font.FontManager;
import com.fpsboost.gui.font.UnicodeFontRenderer;
import com.fpsboost.util.CPSCounter;
import com.fpsboost.util.render.ColorUtil;
import com.fpsboost.util.render.RoundedUtil;
import com.fpsboost.value.impl.BooleanValue;
import com.fpsboost.value.impl.NumberValue;

import java.awt.*;


public class CPSInfo {

    private final BooleanValue backgroundValue = new BooleanValue("背景",true);
    private final NumberValue opacity = new NumberValue("背景不透明度", 0.6, 0.5, 1, .05);
    private final NumberValue backgroundRadiusValue = new NumberValue("背景圆角值", 2,0,10,1);
    UnicodeFontRenderer fontRenderer = FontManager.M18;
    private final Dragging pos = Access.getInstance().getDragManager().createDrag(this.getClass(), "cpsinfo", 150, 150);


    @EventTarget
    public void draw(Render2DEvent event) {
        float x = pos.getXPos();
        float y = pos.getYPos();
        String text = String.format("CPS : %s | %s", CPSCounter.getCPS(CPSCounter.MouseButton.LEFT), CPSCounter.getCPS(CPSCounter.MouseButton.RIGHT));
        Color color = ColorUtil.applyOpacity(Color.BLACK, opacity.getValue().floatValue());
        if (backgroundValue.getValue()) RoundedUtil.drawRound(x,y,fontRenderer.getStringWidth(text) + 1.5F,fontRenderer.getHeight(),backgroundRadiusValue.getValue().intValue(),color);
        pos.setWH(fontRenderer.getStringWidth(text),fontRenderer.getHeight());
        fontRenderer.drawStringWithShadow(text, x, y + 1.5,-1);
    }
}
