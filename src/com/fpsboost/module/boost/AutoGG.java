package com.fpsboost.module.boost;

import com.fpsboost.Access;
import com.fpsboost.annotations.event.EventTarget;
import com.fpsboost.annotations.system.Module;
import com.fpsboost.events.misc.WorldLoadEvent;
import com.fpsboost.events.movement.MotionUpdateEvent;
import com.fpsboost.module.Category;

@Module(value = "自动发GG",category = Category.Boost)
public class AutoGG implements Access.InstanceAccess {
    private boolean active;

    @EventTarget
    public void onPreMotionEvent(MotionUpdateEvent event) {
        if (!event.isPre()) return;
        if (mc.thePlayer.ticksExisted % 18 != 0 || mc.thePlayer.ticksExisted < 20 * 20 || !active || !mc.thePlayer.sendQueue.doneLoadingTerrain) return;

        if (mc.theWorld.playerEntities.stream().filter(entityPlayer -> !entityPlayer.isInvisible()).count() <= 1) {
            active = false;

            mc.thePlayer.sendChatMessage("gg");
        }
    }

    @EventTarget
    public void onWorld(WorldLoadEvent event) {
        active = true;
    }
}
