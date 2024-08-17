package com.fpsboost.module.boost;

import com.fpsboost.Access;
import com.fpsboost.annotations.event.EventTarget;
import com.fpsboost.annotations.system.Module;
import com.fpsboost.annotations.system.Startup;
import com.fpsboost.events.update.UpdateEvent;
import com.fpsboost.module.Category;

@Startup
@Module(name = "NoClickDelay",description = "移除特殊的点击延迟", category = Category.Boost)
public class NoClickDelay implements Access.InstanceAccess {

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (mc.theWorld != null && mc.thePlayer != null) {
            if (!mc.inGameHasFocus) return;

            mc.leftClickCounter = 0;
        }
    }
}