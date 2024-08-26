package com.fpsboost.gui.drag.impl.text.impl;

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

/**
 * @author LangYa
 * @since 2024/8/26 21:55
 */
@Module(name = "BoxingDisplay",cnName = "Boxing显示",description = "显示格式 Boxing : 对面被打的数量 | 我被打的数量",category = Category.GUI)
public class BoxingInfo extends TextDisplay {

    private final BooleanValue backgroundValue = new BooleanValue("背景",true);
    private final ColorValue colorValue = new ColorValue("背景颜色",new Color(0,0,0));
    private final NumberValue opacity = new NumberValue("背景不透明度", 0.25, 0.0, 1, .05);
    private final NumberValue backgroundRadiusValue = new NumberValue("背景圆角值", 2,0,10,1);

    public static Entity target;
    public static int boxing = 0;
    public static int thePlayerBoxing = 0;

    public BoxingInfo() {
        super("BoxingInfo");
    }

    @EventTarget
    public void onTick(TickEvent e) {
        if (mc.thePlayer == null) return;

        if (target != null && target.isEntityAlive() && target.hurtResistantTime == 19) {
            boxing++;
        }
        if (mc.thePlayer.hurtResistantTime == 19 && mc.thePlayer.isEntityAlive()) {
            thePlayerBoxing++;
        }
    }

    @EventTarget
    public void onWorld(WorldLoadEvent event) {
        boxing = 0;
        thePlayerBoxing = 0;
        target = null;
    }

    @EventTarget
    public void onAttack(AttackEvent e) {
        target = e.getTarget();
    }

    @EventTarget
    public void onRender2D(Render2DEvent event) {
        String text = String.format("Boxing: %s |%s", boxing,thePlayerBoxing);
        draw(text,backgroundValue.getValue(),colorValue.getValue(),opacity.getValue().floatValue(),backgroundRadiusValue.getValue().floatValue());
    }

}
