package com.fpsboost.module.render;

import com.fpsboost.annotations.system.Module;
import com.fpsboost.module.Category;
import com.fpsboost.value.impl.NumberValue;

@Module(value = "自定义手持物品",category = Category.GUI)
public class CustomHeldItem {
    public static final NumberValue x = new NumberValue("自定义X", 0, -50, 50, 1);
    public static final NumberValue y = new NumberValue("自定义Y", 0, -50, 50, 1);
    public static final NumberValue size = new NumberValue("自定义大小", 0, 50, -50, 1);
}