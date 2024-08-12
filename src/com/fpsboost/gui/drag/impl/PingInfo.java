package com.fpsboost.gui.drag.impl;

import com.fpsboost.Access;
import com.fpsboost.annotations.event.EventTarget;
import com.fpsboost.annotations.system.Module;
import com.fpsboost.events.render.Render2DEvent;
import com.fpsboost.gui.drag.Dragging;
import com.fpsboost.gui.font.FontManager;
import com.fpsboost.gui.font.UnicodeFontRenderer;
import com.fpsboost.module.Category;
import com.fpsboost.util.render.ColorUtil;
import com.fpsboost.util.render.RoundedUtil;
import com.fpsboost.value.impl.BooleanValue;
import com.fpsboost.value.impl.NumberValue;
import net.minecraft.client.Minecraft;

import java.awt.*;

@Module(value = "Ping显示",category = Category.GUI)
public class PingInfo extends TextDisplay implements Access.InstanceAccess {

    private final BooleanValue backgroundValue = new BooleanValue("背景",true);
    private final NumberValue opacity = new NumberValue("背景不透明度", 0.25, 0.0, 1, .05);
    private final NumberValue backgroundRadiusValue = new NumberValue("背景圆角值", 2,0,10,1);

    public PingInfo() {
        super("PingInfo");
    }


    @EventTarget
    public void draw(Render2DEvent event) {
        String text;

        if (mc.getCurrentServerData() != null) {
            text = String.format("%sms",((int) mc.getCurrentServerData().pingToServer));
        } else {
            text = "0ms";
        }
       draw(text,backgroundValue.getValue(),opacity.getValue().floatValue(),backgroundRadiusValue.getValue().floatValue());
    }
}
