package com.fpsboost.gui.drag.impl.text.impl;

import com.fpsboost.Access;
import com.fpsboost.annotations.event.EventTarget;
import com.fpsboost.annotations.system.Module;
import com.fpsboost.events.misc.AttackEvent;
import com.fpsboost.events.render.Render2DEvent;
import com.fpsboost.events.update.TickEvent;
import com.fpsboost.gui.drag.impl.text.TextDisplay;
import com.fpsboost.module.Category;
import com.fpsboost.value.impl.BooleanValue;
import com.fpsboost.value.impl.ColorValue;
import com.fpsboost.value.impl.NumberValue;
import net.minecraft.entity.Entity;

import java.awt.*;

@Module(name = "TimeDisplay",cnName = "",description = "显示当前的时间(24小时制)",category = Category.GUI)
public class TimeInfo extends TextDisplay implements Access.InstanceAccess {

    private final BooleanValue backgroundValue = new BooleanValue("背景",true);
    private final ColorValue colorValue = new ColorValue("背景颜色",new Color(0,0,0));
    private final NumberValue opacity = new NumberValue("背景不透明度", 0.25, 0.0, 1, .05);
    private final NumberValue backgroundRadiusValue = new NumberValue("背景圆角值", 2,0,10,1);

    public static Entity target;
    public static int combo = 0;

    public TimeInfo() {
        super("TimeInfo");
    }

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
        String text = String.format("TimeInfo: %s", combo);
        draw(text,backgroundValue.getValue(),colorValue.getValue(),opacity.getValue().floatValue(),backgroundRadiusValue.getValue().floatValue());
    }


}
