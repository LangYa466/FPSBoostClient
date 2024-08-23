package com.fpsboost.module.boost;

import com.fpsboost.Access;
import com.fpsboost.annotations.system.*;
import com.fpsboost.module.ModuleManager;
import com.fpsboost.util.ChatUtil;
import com.fpsboost.annotations.event.EventTarget;
import com.fpsboost.command.CommandManager;
import com.fpsboost.events.update.TickEvent;
import com.fpsboost.module.Category;
import org.lwjgl.input.Keyboard;

@Startup
@Module(name = "Sprint",cnName = "自动疾跑",description = "自动疾跑 其实妖猫的布吉岛mod抄袭了我的疾跑",category = Category.Boost)
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
