package com.fpsboost.util;

import com.fpsboost.Access;
import net.minecraft.util.ChatComponentText;

/**
 * @author LangYa
 * @since 2024/6/8 下午5:04
 */

public class ClientUtil implements Access.InstanceAccess {

    public static void chat(String text) {
        if (mc.thePlayer == null) return;
        mc.thePlayer.addChatMessage(new ChatComponentText(text));
    }

    public static void chatPrefix(String text) {
        chat("[FPSBoost] " + text);
    }
}
