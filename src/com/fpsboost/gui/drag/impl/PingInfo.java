package com.fpsboost.gui.drag.impl;

import com.fpsboost.Access;
import com.fpsboost.annotations.event.EventTarget;
import com.fpsboost.annotations.system.Module;
import com.fpsboost.events.render.Render2DEvent;
import com.fpsboost.gui.drag.Dragging;
import com.fpsboost.gui.font.FontManager;
import com.fpsboost.gui.font.UnicodeFontRenderer;
import com.fpsboost.module.Category;
import com.fpsboost.util.render.RoundedUtil;
import com.fpsboost.value.impl.BooleanValue;
import com.fpsboost.value.impl.NumberValue;
import net.minecraft.client.Minecraft;

import java.awt.*;

@Module(value = "Ping显示",category = Category.GUI)
public class PingInfo implements Access.InstanceAccess {

    private final BooleanValue backgroundValue = new BooleanValue("背景",true);
    private final NumberValue backgroundRadiusValue = new NumberValue("背景自圆角值", 2,0,10,1);
    UnicodeFontRenderer fontRenderer = FontManager.M18;
    private final Dragging pos = Access.getInstance().getDragManager().createDrag(this.getClass(), "pinginfo", 33, 33);


    @EventTarget
    public void draw(Render2DEvent event) {
        float x = pos.getXPos();
        float y = pos.getYPos();
        String text;

        if (mc.getCurrentServerData() != null) {
            text = String.format("%sms",((int) mc.getCurrentServerData().pingToServer));
        } else {
            text = "0ms";
        }

        if (backgroundValue.getValue()) RoundedUtil.drawRound(x,y,fontRenderer.getStringWidth(text) + 1.5F,fontRenderer.getHeight(),backgroundRadiusValue.getValue().intValue(),new Color(0,0,0,120));
        pos.setWH(fontRenderer.getStringWidth(text),fontRenderer.getHeight());
        fontRenderer.drawStringWithShadow(text, x, y + 1.5,-1);
    }
}
