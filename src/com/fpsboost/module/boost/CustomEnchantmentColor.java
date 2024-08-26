package com.fpsboost.module.boost;

import com.fpsboost.annotations.system.Module;
import com.fpsboost.module.Category;
import com.fpsboost.value.impl.ColorValue;

import java.awt.*;

/**
 * @author LangYa
 * @since 2024/8/26 22:12
 */
@Module(name = "CustomEnchantmentColor",cnName = "自定义附魔颜色",description = "自定义附魔效果的颜色",category = Category.Boost)
public class CustomEnchantmentColor {
    public static final ColorValue color = new ColorValue("自定义颜色",new Color(-8372020));
}
