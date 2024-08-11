package com.fpsboost.util;

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import com.fpsboost.Access;

public class ChatUtil implements Access.InstanceAccess {
    public static final String PRIMARY_COLOR = EnumChatFormatting.BLUE.toString();
    public static final String SECONDARY_COLOR = EnumChatFormatting.GRAY.toString();
    private static final String PREFIX = PRIMARY_COLOR + "[" + SECONDARY_COLOR + Access.CLIENT_NAME + PRIMARY_COLOR + "] ";

    private static void send(final String message) {
        if (mc.thePlayer == null) return;
        mc.thePlayer.addChatMessage(new ChatComponentText(message));
    }

    public static void success(String s) {
        info(s);
    }

    public static void info(String s) {
        send(PREFIX + s);
    }
}
