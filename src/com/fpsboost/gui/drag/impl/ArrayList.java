package com.fpsboost.gui.drag.impl;

import com.fpsboost.Access;
import com.fpsboost.annotations.event.EventTarget;
import com.fpsboost.annotations.system.Init;
import com.fpsboost.annotations.system.Module;
import com.fpsboost.events.render.Render2DEvent;
import com.fpsboost.gui.drag.Dragging;
import com.fpsboost.gui.font.FontManager;
import com.fpsboost.module.Category;
import com.fpsboost.util.render.ColorUtil;
import com.fpsboost.value.impl.ComboValue;
import com.fpsboost.value.impl.NumberValue;

import java.awt.*;

@Module(value = "仿外挂列表",category = Category.GUI)
public class ArrayList implements Access.InstanceAccess {

    private static final ComboValue textMode = new ComboValue("语言", "中文", "中文", "英文");
    private static final ComboValue colorMode = new ComboValue("颜色", "彩虹", "自定义", "彩虹");
    private static final NumberValue customColorRed = new NumberValue("自定义红色", 0, 0, 255, 5);
    private static final NumberValue customColorGreen = new NumberValue("自定义绿色", 0, 0, 255, 5);
    private static final NumberValue customColorBlue = new NumberValue("自定义蓝色", 0, 0, 255, 5);
    private final NumberValue spacing = new NumberValue("间距", 3, 1, 5, 1);

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
            Color c = Color.white;
            switch (colorMode.getValue()) {
                case "自定义":
                    c = new Color(customColorRed.getValue().intValue(), customColorGreen.getValue().intValue(), customColorBlue.getValue().intValue());
                    break;
                case "彩虹":
                    c = ColorUtil.rainbow();
            }
            if (textMode.isMode("中文")) {
                FontManager.M22.drawStringWithShadow(module.getSimpleName(), x , y + y1, c.getRGB());
            } else {
                FontManager.M22.drawStringWithShadow(access.getModuleManager().format(module), x , y + y1, c.getRGB());
            }
            y1 += FontManager.M22.getHeight() + spacing.getValue().intValue();
        }
    }


}
