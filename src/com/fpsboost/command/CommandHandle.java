package com.fpsboost.command;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract Command
 *
 * @author cubk
 */
public abstract class CommandHandle {
	private final List<Method> handlers = new ArrayList<>();

	abstract void run(String[] args);

	abstract String usage();

	public List<Method> getHandlers() {
		return handlers;
	}
}
