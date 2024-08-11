package com.fpsboost.command.commands;

import com.fpsboost.Access;
import com.fpsboost.annotations.system.Command;
import com.fpsboost.util.ChatUtil;
import org.lwjgl.input.Keyboard;
import com.fpsboost.module.boost.Sprint;

@Command(value = "bind",usage = "bind <module> <key>")
public class BindCommand implements Access.InstanceAccess {

	@Command.Handler
	public void secondHandler(String[] args, Sprint sprint) {
		ChatUtil.info("Received " + args.length + " parameter(s)!");
		ChatUtil.info("Sprint class is " + sprint.getClass().getName());
	}

	@Command.Handler
	public void run(String[] args) {
		if (args.length == 3) {
			Class<?> module = access.getModuleManager().getModuleClass(args[1]);
			if (module != null) {
				access.getModuleManager().setKey(module, Keyboard.getKeyIndex(args[2].toUpperCase()));
				ChatUtil.info(access.getModuleManager().getName(module) + " has been bound to " + args[2] + ".");
			} else {
				ChatUtil.info(args[1] + " not found.");
			}
		}else {
			ChatUtil.info(usage(this));
		}
	}
}
