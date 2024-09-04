package com.fpsboost.module.render;

import com.fpsboost.annotations.system.Disable;
import com.fpsboost.annotations.system.Enable;
import com.fpsboost.annotations.system.Module;
import com.fpsboost.module.Category;
import com.fpsboost.value.impl.BooleanValue;
import com.fpsboost.value.impl.NumberValue;

/**
 * @author LangYa
 * @since 2024/9/4 20:12
 */
@Module(name = "CustomScoreboard",category = Category.GUI,cnName = "自定义记分板",description = "自定义记分板圆角和其他内容")
public class CustomScoreboard {
    public static final BooleanValue displayRect = new BooleanValue("背景显示",true);
    public static final BooleanValue displayReadLine = new BooleanValue("红色数字显示",false);
    public static final NumberValue customRadius = new NumberValue("背景圆角值", 2,0,10,1);

    //省性能
    public static boolean isEnable;

    @Enable
    public void onEnable() {
        isEnable = true;
    }
    @Disable
    public void onDisable() {
        isEnable = false;
    }

}
