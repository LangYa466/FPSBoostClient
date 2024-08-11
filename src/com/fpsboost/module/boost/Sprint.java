package com.fpsboost.module.boost;

import com.fpsboost.Access;
import com.fpsboost.annotations.system.Disable;
import com.fpsboost.annotations.system.Enable;
import com.fpsboost.annotations.system.Module;
import com.fpsboost.module.ModuleManager;
import com.fpsboost.util.ChatUtil;
import com.fpsboost.annotations.event.EventTarget;
import com.fpsboost.command.CommandManager;
import com.fpsboost.events.update.TickEvent;
import com.fpsboost.module.Category;

@Module(value = "自动疾跑",category = Category.Boost)
public class Sprint implements Access.InstanceAccess {

    private final ModuleManager moduleManager;

    public Sprint(ModuleManager moduleManager) {
        this.moduleManager = moduleManager;
    }

    /**
     * Subscribe a {@link TickEvent}
     *
     * @param event Event
     */
    @EventTarget
    public void onUpdate(TickEvent event, CommandManager commandManager) {
        mc.gameSettings.keyBindSprint.pressed = true;
    }
}
