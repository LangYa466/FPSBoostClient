package com.fpsboost.irc;

import com.fpsboost.Access;
import com.fpsboost.annotations.event.EventTarget;
import com.fpsboost.annotations.system.Disable;
import com.fpsboost.annotations.system.Module;
import com.fpsboost.annotations.system.Startup;
import com.fpsboost.events.misc.WorldLoadEvent;
import com.fpsboost.irc.client.IRCTransport;
import com.fpsboost.module.Category;

/**
 * @author LangYa
 * @since 2024/11/24 下午3:59
 */
@Startup
@Module(name = "UserDisplay",cnName = "用户显示",category = Category.Boost)
public class IRC implements Access.InstanceAccess {

    @Disable
    public void onDisable() {
        setEnable(this,true);
    }

    @EventTarget
    public static boolean sendIRCMessage(String message) {
        IRCTransport transport = Access.getInstance().getIrcManager().transport;
        if (message.startsWith("-")) {
            String ircMessage = message.replace("-","");
            if (transport != null) transport.sendChat(ircMessage);
            return true;
        }
        return false;
    }

    @EventTarget
    public void onWorldLoad(WorldLoadEvent event) {
        IRCTransport transport = Access.getInstance().getIrcManager().transport;
        if (transport != null) transport.sendInGameUsername();
    }
}