package com.fpsboost.module.boost;

import com.fpsboost.Access;
import com.fpsboost.annotations.event.EventTarget;
import com.fpsboost.annotations.system.Module;
import com.fpsboost.annotations.system.Startup;
import com.fpsboost.events.update.TickEvent;
import com.fpsboost.module.Category;

@Startup
@Module(value = "无点击延迟",category = Category.Boost)
public class NoHitDelay implements Access.InstanceAccess {

    @EventTarget
    public void onTick(TickEvent event) {
        mc.leftClickCounter = 0;
    }
}
