package com.fpsboost.module.boost;

import com.fpsboost.annotations.system.Disable;
import com.fpsboost.annotations.system.Enable;
import com.fpsboost.annotations.system.Module;
import com.fpsboost.module.Category;
import com.fpsboost.util.mousefix.RawInputMod;

@Module(value = "原始鼠标数据",category = Category.Boost)
public class MouseFix {
    private final RawInputMod rawInputMod = new RawInputMod();

    @Enable
    public void onEnable() {
        rawInputMod.start();
    }

    @Disable
    public void onDisable() {
        rawInputMod.stop();
    }

}