package com.fpsboost.util;

import com.fpsboost.Access;

public class SoundUtil implements Access.InstanceAccess {

    public static void playSound(final String sound) {
        playSound(sound, 1, 1);
    }

    public static void playSound(final String sound, final float volume, final float pitch) {
        mc.theWorld.playSound(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, sound, volume, pitch, false);
    }
}