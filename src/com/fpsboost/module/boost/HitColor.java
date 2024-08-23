package com.fpsboost.module.boost;

import com.fpsboost.annotations.system.Module;
import com.fpsboost.module.Category;
import com.fpsboost.value.impl.ColorValue;
import com.fpsboost.value.impl.NumberValue;

import java.awt.*;

/**
 * @author LangYa
 * @since 2024/07/26/下午3:24
 */
@Module(name = "HitColor",cnName = "修改受伤颜色",description = "修改受伤的颜色",category = Category.Boost)
public class HitColor {
    public static final ColorValue colorValue = new ColorValue("背景颜色",new Color(0,0,0));
    public static final NumberValue alphaValue = new NumberValue("不透明度",0.3,0,1,0.01);
}
