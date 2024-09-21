package com.fpsboost.module.boost;

import com.fpsboost.Access;
import com.fpsboost.annotations.event.EventTarget;
import com.fpsboost.annotations.system.Module;
import com.fpsboost.events.render.Render2DEvent;
import com.fpsboost.gui.font.FontManager;
import com.fpsboost.module.Category;
import com.fpsboost.util.render.ColorUtil;
import com.fpsboost.value.impl.NumberValue;

import java.text.DecimalFormat;

/**
 * @author LangYa
 * @since 2024/9/10 13:13
 */
@Module(name = "HealthWarn",cnName = "血量提示",description = "低血量的时候提示",category = Category.Boost)
public class HealthWarn implements Access.InstanceAccess {

    private final NumberValue healthValue = new NumberValue("血量",7,1,20,1);
    private final DecimalFormat decimalFormat = new DecimalFormat("0.0");

    @EventTarget
    public void onRender2D(Render2DEvent event) {
        float health = mc.thePlayer.getHealth();
        String text = String.format("你快死了 剩下%s滴血",decimalFormat.format(health));
        if (health <= healthValue.getValue())
            FontManager.M20.drawStringWithShadow(text,event.getScaledResolution().getScaledWidth() / 2F - FontManager.M22.getStringWidth(text) / 2.25F,
                    event.getScaledResolution().getScaledHeight() / 2F + 5, ColorUtil.healthColor(health,mc.thePlayer.getMaxHealth()).getRGB());

    }
}
