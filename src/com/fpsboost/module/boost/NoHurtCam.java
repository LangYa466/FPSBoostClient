package com.fpsboost.module.boost;

import com.fpsboost.annotations.event.EventTarget;
import com.fpsboost.annotations.system.Module;
import com.fpsboost.events.render.HurtCamEvent;
import com.fpsboost.module.Category;

/**
 * @author LangYa466
 * @since 2024/5/11 22:04
 */

@Module(name = "NoHurtCam",cnName = "无受伤抖动",description = "无受伤抖动效果",category = Category.Boost)
public class NoHurtCam {

    @EventTarget
    public void onHurtCam(HurtCamEvent event) {
        event.setCancelled(true);
    }

}
