package com.fpsboost.module.render;

import com.fpsboost.annotations.system.Module;
import com.fpsboost.module.Category;
import com.fpsboost.value.impl.BooleanValue;

@Module(value = "旧版本动画",category = Category.GUI)
public class OldAnimation {
    public static BooleanValue oldRod = new BooleanValue("鱼竿动画", false);
    public static BooleanValue oldBlock = new BooleanValue("放置动画", false);
    public static BooleanValue blockHit = new BooleanValue("防砍动画", false);
    public static BooleanValue oldBow = new BooleanValue("弓箭动画", false);
    public static BooleanValue oldSwing = new BooleanValue("挥手动画", false);
}