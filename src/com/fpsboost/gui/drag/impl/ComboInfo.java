package com.fpsboost.gui.drag.impl;

import com.fpsboost.Access;
import com.fpsboost.annotations.event.EventTarget;
import com.fpsboost.annotations.system.Module;
import com.fpsboost.events.misc.AttackEvent;
import com.fpsboost.events.render.Render2DEvent;
import com.fpsboost.events.update.TickEvent;
import com.fpsboost.gui.drag.Dragging;
import com.fpsboost.gui.font.FontManager;
import com.fpsboost.gui.font.UnicodeFontRenderer;
import com.fpsboost.module.Category;
import com.fpsboost.util.render.RoundedUtil;
import com.fpsboost.value.impl.BooleanValue;
import com.fpsboost.value.impl.NumberValue;
import net.minecraft.entity.Entity;

import java.awt.*;

@Module(value = "连击显示",category = Category.GUI)
public class ComboInfo implements Access.InstanceAccess {

    private final BooleanValue backgroundValue = new BooleanValue("背景",true);
    private final NumberValue backgroundRadiusValue = new NumberValue("背景自圆角值", 2,0,10,1);
    private final Dragging pos = Access.getInstance().getDragManager().createDrag( this.getClass(),"comboinfo", 50, 50);

    private final UnicodeFontRenderer fontRenderer = FontManager.M18;

    public static Entity target;
    public static int combo = 0;

    @EventTarget
    public void onTick(TickEvent e) {
        if (mc.thePlayer == null) return;

        if (mc.thePlayer.hurtTime == 1 || (target != null && mc.thePlayer.getDistanceToEntity(target) > 5)) {
            combo = 0;
        }

        if (target != null && target.isEntityAlive() && target.hurtResistantTime == 19) {
            combo++;
        }
    }

    @EventTarget
    public void onAttack(AttackEvent e) {
        target = e.getTarget();
    }


    @EventTarget
    public void onRender2D(Render2DEvent event) {
        float x = pos.getXPos();
        float y = pos.getYPos();
        String text = String.format("Combo: %s", combo);
        if (backgroundValue.getValue()) RoundedUtil.drawRound(x,y,fontRenderer.getStringWidth(text) + 1.5F,fontRenderer.getHeight(),backgroundRadiusValue.getValue().intValue(),new Color(0,0,0,120));
        pos.setWH(fontRenderer.getStringWidth(text),fontRenderer.getHeight());
        fontRenderer.drawStringWithShadow(text, x, y + 1.5,-1);
    }


}
