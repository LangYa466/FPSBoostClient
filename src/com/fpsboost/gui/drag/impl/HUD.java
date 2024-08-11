package com.fpsboost.gui.drag.impl;

import com.fpsboost.Access;
import com.fpsboost.annotations.event.EventTarget;
import com.fpsboost.annotations.system.Module;
import com.fpsboost.annotations.system.Startup;
import com.fpsboost.gui.drag.Dragging;
import com.fpsboost.gui.font.FontManager;
import com.fpsboost.gui.font.UnicodeFontRenderer;
import com.fpsboost.module.ModuleManager;
import com.fpsboost.module.boost.Sprint;
import com.fpsboost.util.LiteInvoke;
import com.fpsboost.events.render.Render2DEvent;
import com.fpsboost.module.Category;
import com.fpsboost.util.render.ColorUtil;
import com.fpsboost.util.render.RoundedUtil;
import com.fpsboost.value.impl.BooleanValue;
import com.fpsboost.value.impl.NumberValue;

import java.awt.*;

@Module(value = "客户端Logo",category = Category.GUI)
@Startup // enable when client startup
public class HUD implements Access.InstanceAccess {

    @LiteInvoke.Autowired
    private Sprint sprint;

    @LiteInvoke.Autowired
    private ModuleManager moduleManager;
    private final BooleanValue backgroundValue = new BooleanValue("背景",false);
    private final NumberValue opacity = new NumberValue("背景不透明度", 0.6, 0.5, 1, .05);
    private final NumberValue backgroundRadiusValue = new NumberValue("背景圆角值", 2,0,10,1);
    private final Dragging pos = Access.getInstance().getDragManager().createDrag(this.getClass(),"logo", 4, 4);

    /**
     * Subscribe a {@link Render2DEvent}
     *
     * @param event Event
     */
    @EventTarget
    public void onRender2D(Render2DEvent event) {
        UnicodeFontRenderer fontRenderer = FontManager.M18;
        String text = "FPSBoost";

        float x = pos.getXPos();
        float y = pos.getYPos();

        Color color = ColorUtil.applyOpacity(Color.BLACK, opacity.getValue().floatValue());

        if (backgroundValue.getValue()) RoundedUtil.drawRound(x,y,fontRenderer.getStringWidth(text) + 1.5F,fontRenderer.getHeight(),backgroundRadiusValue.getValue().intValue(),color);
        pos.setWH(fontRenderer.getStringWidth(text),fontRenderer.getHeight());
        fontRenderer.drawStringWithShadow(text, x, y + 1.5,-1);
    }

}
