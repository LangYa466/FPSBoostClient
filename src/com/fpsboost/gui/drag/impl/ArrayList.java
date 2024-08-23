package com.fpsboost.gui.drag.impl;

import com.fpsboost.Access;
import com.fpsboost.annotations.event.EventTarget;
import com.fpsboost.annotations.system.Module;
import com.fpsboost.events.render.Render2DEvent;
import com.fpsboost.gui.drag.Dragging;
import com.fpsboost.gui.font.FontManager;
import com.fpsboost.module.Category;
import com.fpsboost.util.render.ColorUtil;
import com.fpsboost.util.render.RoundedUtil;
import com.fpsboost.value.impl.BooleanValue;
import com.fpsboost.value.impl.ColorValue;
import com.fpsboost.value.impl.NumberValue;
import net.minecraft.client.gui.Gui;

import java.awt.*;


@Module(name = "ModuleList",cnName = "功能列表",description = "模仿外挂的ArrayList就是显示全部功能",category = Category.GUI)
public class ArrayList implements Access.InstanceAccess {

    private final ColorValue textColor = new ColorValue("文本颜色",new Color(0,0,0));
    private final NumberValue spacing = new NumberValue("间距", 3, 1, 5, 1);
    private final BooleanValue background = new BooleanValue("背景",true);
    private final NumberValue opacity = new NumberValue("背景透明度", 0.25, 0.0, 1, .05);
    private final NumberValue radius = new NumberValue("背景圆角", 3, 1, 17.5, .5);
    private final ColorValue bgColor = new ColorValue("背景颜色",new Color(0,0,0));

    private final Dragging pos = Access.getInstance().getDragManager().createDrag(this.getClass(),"arraylist", 4, 15);

    @EventTarget
    public void onRender2D(Render2DEvent event) {
        int y1 = 4;
        int width = 0;
        float x = pos.getXPos();
        float y = pos.getYPos();
        java.util.ArrayList<Class<?>> enabledModules = new java.util.ArrayList<>();
        for (Class<?> m : access.getModuleManager().getModules()) {
            if (!Access.getInstance().getModuleManager().isVisible(m)) continue;
            if (access.getModuleManager().isEnabled(m) && access.getModuleManager().isVisible(m)) {
                int widthM = FontManager.M22.getStringWidth(access.getModuleManager().format(m));
                if (width < widthM) {
                    width = widthM;
                    pos.setWidth(width);
                }
                enabledModules.add(m);
                pos.setHeight(FontManager.M22.getHeight() * enabledModules.size());
            }
        }
        enabledModules.sort((o1, o2) -> FontManager.M22.getStringWidth(access.getModuleManager().format(o2)) - FontManager.M22.getStringWidth(access.getModuleManager().format(o1)));

        for (Class<?> module : enabledModules) {
            String displayText = access.getModuleManager().format(module);
            if (background.getValue()) {
                float offset = 5;
                Color color = bgColor.getValue();
                float width2 = FontManager.M22.getStringWidth(displayText);
                RoundedUtil.drawRound(x - 2, y + y1, width2 + offset, FontManager.M22.getHeight(),radius.getValue().intValue(), ColorUtil.applyOpacity(color, opacity.getValue().floatValue()));
            }
            FontManager.M22.drawStringWithShadow(displayText, x , y + y1 + 2F, textColor.getValue().getRGB());
            y1 += FontManager.M22.getHeight() + spacing.getValue().intValue();
        }
    }


}
