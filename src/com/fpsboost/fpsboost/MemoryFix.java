package com.fpsboost.fpsboost;

import com.fpsboost.annotations.event.EventTarget;
import com.fpsboost.events.misc.WorldLoadEvent;

/**
 * @author LangYa
 * @since 2024/8/29 0:31
 */
public class MemoryFix {
    @EventTarget
    public void onWorldLoad(WorldLoadEvent event) {
        System.gc();
    }
}
