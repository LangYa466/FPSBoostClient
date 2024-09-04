package com.fpsboost.module.render;

import com.fpsboost.annotations.system.Disable;
import com.fpsboost.annotations.system.Enable;
import com.fpsboost.annotations.system.Module;
import com.fpsboost.module.Category;
import com.fpsboost.value.impl.NumberValue;

@Module(name = "CustomHeldItem",cnName = "自定义手持物品",description = "自定义你的手持物品XY和大小",category = Category.GUI)
public class CustomHeldItem {
    public static final NumberValue x = new NumberValue("自定义X", 0, -50, 50, 1);
    public static final NumberValue y = new NumberValue("自定义Y", 0, -50, 50, 1);
    public static final NumberValue size = new NumberValue("自定义大小", 0, 50, -50, 1);

    //如果直接在改xy size的方法里面直接获取会浪费性能
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
