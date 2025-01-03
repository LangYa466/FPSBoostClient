package com.fpsboost;

import com.fpsboost.api.viamcp.ViaMCP;
import com.fpsboost.events.EventManager;
import com.fpsboost.fpsboost.MemoryFix;
import com.fpsboost.gui.drag.DragManager;
import com.fpsboost.irc.IRCManager;
import com.fpsboost.module.ModuleManager;
import com.fpsboost.module.RankManager;
import com.fpsboost.module.WingsManager;
import com.fpsboost.module.boost.DragonWings;
import com.fpsboost.plugin.PluginManager;
import com.fpsboost.util.HWIDUtil;
import com.fpsboost.util.LiteInvoke;
import com.fpsboost.util.WebUtils;
import net.minecraft.client.Minecraft;
import com.fpsboost.annotations.system.Command;
import com.fpsboost.command.CommandManager;

import com.fpsboost.gui.clickGui.drop.ClickGuiScreen;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.File;
import java.security.NoSuchAlgorithmException;

/**
 * Client Entry
 * The main class where the client is loaded up.
 * Anything related to the client will start from here and managers etc instances will be stored in this class.
 *
 * @author cubk
 */
@LiteInvoke.Instance
public final class Access {

    public static final String CLIENT_NAME = "FPSBoost Client";
    public static final String CLIENT_VERSION = "1.64";
    public static final String CLIENT_WEBSITE = "http://122.51.47.169/";
    public static final File DIRECTORY = new File(Minecraft.getMinecraft().mcDataDir, "FPSBoostClient");
    public static ClientMode MODE = ClientMode.PC;
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
     * DragManager Instance
     */
    private final DragManager dragManager;

    /**
     * PluginManager Instance
     */
    private final PluginManager pluginManager;

    /**
     * ClickGui Instance
     */
    private final ClickGuiScreen clickGui;
    private final RankManager rankManager;
    private final IRCManager ircManager;

    /*
    * https://github.com/cubk1/LiteInvoke
    * Dependency injection utility
    */
    private final LiteInvoke invoke;

    public static void displayTray(String title, String text, TrayIcon.MessageType type) {
        SystemTray tray = SystemTray.getSystemTray();
        Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
        TrayIcon trayIcon = new TrayIcon(image, "Tray Demo");
        trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip("System tray icon demo");
        try {
            tray.add(trayIcon);
            trayIcon.displayMessage(title, text, type);
        } catch (AWTException e) {
            System.err.println("TrayIcon could not be added.");
        }
    }


    public static void displayTray(String title, String text) {
        displayTray(title,text, TrayIcon.MessageType.INFO);
    }

    /**
     * Entry point
     */
    public Access() {


        if (!WebUtils.get(CLIENT_WEBSITE + "version.txt").contains(CLIENT_VERSION)) {
            displayTray("您的版本不是最新版","出现BUG请勿反馈");
        }

        try {
            if (WebUtils.get(CLIENT_WEBSITE + "wing.txt").contains(HWIDUtil.getHWID())) {
                DragonWings.location = new ResourceLocation("client/neonwings.png");
            }
        } catch (NoSuchAlgorithmException e) {
            System.out.println("获取饰品失败");
        }

        INSTANCE = this;

        // Initialize managers
        invoke = new LiteInvoke();
        moduleManager = new ModuleManager();
        commandManager = new CommandManager();
        dragManager = new DragManager();
        clickGui = new ClickGuiScreen();
        pluginManager = new PluginManager();
        ircManager = new IRCManager();

        try {
            invoke. registerFields(this);
        } catch (Exception e) {
           // e.printStackTrace();
        }

        pluginManager.init();
        moduleManager.init();
        moduleManager.loadConfig("module.json");
        commandManager.init();
        clickGui.init();
        rankManager = new RankManager();
        ircManager.init();
        // configManager.getConfigs().forEach(config -> configManager.loadConfig(config.name));
        EventManager.register(dragManager);
        EventManager.register(moduleManager);
        EventManager.register(new MemoryFix());

        // Init ViaMCP
        try {
            ViaMCP.create();

            // In case you want a version slider like in the Minecraft options, you can use this code here, please choose one of those:

            ViaMCP.INSTANCE.initAsyncSlider(); // For top left aligned slider
            ViaMCP.INSTANCE.initAsyncSlider(5, 2, 110, 20);
        } catch (Exception e) {
            e.printStackTrace();
        }
       // new WingsManager();
    }

    public static void toggle() {
        if (MODE.equals(ClientMode.PC)) {
            MODE = ClientMode.PE;
        } else if (MODE.equals(ClientMode.PE)) {
            MODE = ClientMode.PC;
        }
    }

    public void onStop() {
        dragManager.saveDragData();
        moduleManager.saveConfig("module.json");
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

    public IRCManager getIrcManager() {
        return ircManager;
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
