package com.fpsboost;

import com.fpsboost.gui.drag.DragManager;
import com.fpsboost.module.ModuleManager;
import com.fpsboost.util.LiteInvoke;
import net.minecraft.client.Minecraft;
import com.fpsboost.annotations.system.Command;
import com.fpsboost.command.CommandManager;

import com.fpsboost.gui.click.ClickGuiScreen;

/**
 * Client Entry
 * The main class where the client is loaded up.
 * Anything related to the client will start from here and managers etc instances will be stored in this class.
 *
 * @author cubk
 */
@LiteInvoke.Instance
public final class Access {

    public static final String CLIENT_VERSION = "1.01";
    public static String CLIENT_NAME = "FPSBoost Client";

    /**
     * Client Instance, access managers with this
     */
    private static Access INSTANCE;

    /**
     * ModuleManager Instance, access modules here
     */
    private final ModuleManager moduleManager;

    /**
     * CommandManager Instance, access commands here
     */
    private final CommandManager commandManager;

    /**
     * DragManager Instance, access commands here
     */
    private final DragManager dragManager;

    /**
     * ClickGui Instance
     */
    private final ClickGuiScreen clickGui;

    /*
    * https://github.com/cubk1/LiteInvoke
    * Dependency injection utility
    */
    private final LiteInvoke invoke;

    /**
     * Entry point
     */
    public Access() {
        INSTANCE = this;

        // Initialize managers
        invoke = new LiteInvoke();
        moduleManager = new ModuleManager();
        commandManager = new CommandManager();
        dragManager = new DragManager();
        clickGui = new ClickGuiScreen();

        try {
            invoke. registerFields(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        moduleManager.init();
        commandManager.init();
        clickGui.init();
       // configManager.getConfigs().forEach(config -> configManager.loadConfig(config.name));

    }


    /**
     * Get client instance
     *
     * @return {@link Access}
     */
    public static Access getInstance() {
        return INSTANCE;
    }

    /**
     * @return {@link LiteInvoke}
     */
    public LiteInvoke getInvoke() {
        return invoke;
    }

    /**
     * Get module manager instance
     *
     * @return {@link ModuleManager}
     */
    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    /**
     * Get command manager instance
     *
     * @return {@link CommandManager}
     */
    public CommandManager getCommandManager() {
        return commandManager;
    }

    /**
     * Get ClickGui Instance
     *
     * @return {@link ClickGuiScreen}
     */
    public ClickGuiScreen getClickGui() {
        return clickGui;
    }

    public DragManager getDragManager() {
        return dragManager;
    }

    /**
     * Implement this class for access instances
     *
     * @author cubk
     */
    public interface InstanceAccess {
        Minecraft mc = Minecraft.getMinecraft();
        Access access = Access.getInstance();

        default void setSuffix(String suffix, Object object) {
            access.getModuleManager().getHandle(object).setSuffix(suffix);
        }

        default void setEnable(Object object, boolean state) {
            access.getModuleManager().getHandle(object).setEnable(state);
        }

        default void setEnable(Class<?> module, boolean state) {
            access.getModuleManager().setEnable(module, state);
        }

        default boolean isEnabled(Class<?> module) {
            return access.getModuleManager().isEnabled(module);
        }

        default String usage(Object command){
            return command.getClass().getAnnotation(Command.class).usage();
        }
    }

}