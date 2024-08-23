package com.fpsboost.module.boost;

import com.fpsboost.annotations.system.Disable;
import com.fpsboost.annotations.system.Enable;
import com.fpsboost.annotations.system.Module;
import net.minecraft.client.Minecraft;
import com.fpsboost.module.Category;

@Module(name = "FullBright",cnName = "夜视",description = "修改伽马值 达到夜视效果",category = Category.Boost)
public class FullBright {

    @Enable
    public void onEnable() {
        Minecraft.getMinecraft().gameSettings.gammaSetting = 300;
    }

    @Disable
    public void onDisable() {
        Minecraft.getMinecraft().gameSettings.gammaSetting = 1;
    }


}
