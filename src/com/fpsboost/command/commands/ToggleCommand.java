package com.fpsboost.command.commands;

import com.fpsboost.Access;
import com.fpsboost.annotations.system.Command;
import com.fpsboost.util.ChatUtil;

@Command(value = {"t","toggle"},usage = "toggle <module>")
public class ToggleCommand implements Access.InstanceAccess {
    @Command.Handler
    public void run(String[] args) {
        if (args.length == 2) {
            Class<?> module = access.getModuleManager().getModuleClass(args[1]);
            if (module != null) {
                boolean state = access.getModuleManager().toggle(module);
                ChatUtil.info("Module has been " + (state ? "Enabled" : "Disabled") + "!");
            } else {
                ChatUtil.info("Module not found");
            }
        } else {
            ChatUtil.info(usage(this));
        }
    }
}
