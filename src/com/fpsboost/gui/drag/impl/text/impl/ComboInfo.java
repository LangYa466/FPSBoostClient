package com.fpsboost.gui.drag.impl.text.impl;

import com.fpsboost.Access;
import com.fpsboost.annotations.event.EventTarget;
import com.fpsboost.annotations.system.Module;
import com.fpsboost.events.misc.AttackEvent;
import com.fpsboost.events.misc.WorldLoadEvent;
import com.fpsboost.events.render.Render2DEvent;
import com.fpsboost.events.update.TickEvent;
import com.fpsboost.gui.drag.impl.text.TextDisplay;
import com.fpsboost.module.Category;
import com.fpsboost.value.impl.BooleanValue;
import com.fpsboost.value.impl.ColorValue;
import com.fpsboost.value.impl.NumberValue;
import net.minecraft.entity.Entity;

import java.awt.*;

@Module(name = "ComboDisplay",cnName = "Combo显示",description = "显示PVP连击数",category = Category.GUI)
public class ComboInfo extends TextDisplay implements Access.InstanceAccess {

    private final BooleanValue backgroundValue = new BooleanValue("背景",true);
    private final ColorValue colorValue = new ColorValue("背景颜色",new Color(0,0,0));
    private final NumberValue opacity = new NumberValue("背景不透明度", 0.25, 0.0, 1, .05);
    private final NumberValue backgroundRadiusValue = new NumberValue("背景圆角值", 2,0,10,1);

    public static Entity target;
    public static int combo = 0;

    public ComboInfo() {
        super("ComboInfo");
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
    public void onWorld(WorldLoadEvent event) {
        combo = 0;
        target = null;
    }

    @EventTarget
    public void onAttack(AttackEvent e) {
        target = e.getTarget();
    }

    @EventTarget
    public void onRender2D(Render2DEvent event) {
        String text = String.format("Combo: %s", combo);
        draw(text,backgroundValue.getValue(),colorValue.getValue(),opacity.getValue().floatValue(),backgroundRadiusValue.getValue().floatValue());
    }


}
