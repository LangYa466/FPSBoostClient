package com.fpsboost.gui.drag.impl.text.impl;

import com.fpsboost.Access;
import com.fpsboost.annotations.event.EventTarget;
import com.fpsboost.annotations.system.Module;
import com.fpsboost.gui.drag.impl.text.TextDisplay;
import com.fpsboost.gui.font.FontManager;
import com.fpsboost.gui.font.UnicodeFontRenderer;
import com.fpsboost.module.ModuleManager;
import com.fpsboost.module.boost.Sprint;
import com.fpsboost.util.LiteInvoke;
import com.fpsboost.events.render.Render2DEvent;
import com.fpsboost.module.Category;
import com.fpsboost.value.impl.BooleanValue;
import com.fpsboost.value.impl.ColorValue;
import com.fpsboost.value.impl.NumberValue;

import java.awt.*;

@Module(name = "LogoDisplay",cnName = "Logo显示",description = "显示客户端的Logo",category = Category.GUI)
public class LogoDisplay extends TextDisplay implements Access.InstanceAccess {

    @LiteInvoke.Autowired
    private Sprint sprint;

    @LiteInvoke.Autowired
    private ModuleManager moduleManager;
    private final BooleanValue backgroundValue = new BooleanValue("背景",false);
    private final ColorValue colorValue = new ColorValue("背景颜色",new Color(0,0,0));
    private final NumberValue opacity = new NumberValue("背景不透明度", 0.25, 0.0, 1, .05);
    private final NumberValue backgroundRadiusValue = new NumberValue("背景圆角值", 2,0,10,1);
    private static final UnicodeFontRenderer fontRenderer = FontManager.S50;

    public LogoDisplay() {
        super("LogoDisplay", fontRenderer);
    }

    /**
     * Subscribe a {@link Render2DEvent}
     *
     * @param event Event
     */
    @EventTarget
    public void onRender2D(Render2DEvent event) {
        String text = "FPSBoost";
        draw(text,backgroundValue.getValue(),colorValue.getValue(),opacity.getValue().floatValue(),backgroundRadiusValue.getValue().floatValue());
    }

}
