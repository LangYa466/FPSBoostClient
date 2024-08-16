package com.fpsboost.module.boost;

import com.fpsboost.annotations.system.Module;
import com.fpsboost.module.Category;
import com.fpsboost.value.impl.NumberValue;

/**
 * @author LangYa
 * @since 2024/07/26/下午3:24
 */
@Module(value = "更改受伤颜色",category = Category.Boost)
public class HitColor {
    public static final NumberValue redValue = new NumberValue("红色",0.8, 0.0, 1, .05);
    public static final NumberValue greenValue = new NumberValue("绿色",0, 0.0, 1, .05);
    public static final NumberValue blueValue = new NumberValue("蓝色",0, 0.0, 1, .05);
    public static final NumberValue alphaValue = new NumberValue("不透明度",0.3,0,1,0.01);
}
