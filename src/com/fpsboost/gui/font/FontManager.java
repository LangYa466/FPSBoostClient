package com.fpsboost.gui.font;

import java.awt.Font;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class FontManager {
    public static UnicodeFontRenderer M14 = getFont(new ResourceLocation("client/fonts/HarmonyOS_Sans_SC_Regular.ttf"), 14);
    public static UnicodeFontRenderer M16 = getFont(new ResourceLocation("client/fonts/HarmonyOS_Sans_SC_Regular.ttf"), 16);
    public static UnicodeFontRenderer M18 = getFont(new ResourceLocation("client/fonts/HarmonyOS_Sans_SC_Regular.ttf"), 18);
    public static UnicodeFontRenderer M22 = getFont(new ResourceLocation("client/fonts/HarmonyOS_Sans_SC_Regular.ttf"), 22);
    public static UnicodeFontRenderer M50 = getFont(new ResourceLocation("client/fonts/HarmonyOS_Sans_SC_Regular.ttf"), 50);

    private static UnicodeFontRenderer getFont(ResourceLocation resourceLocation, float fontSize) {
        try {
            Font output = Font.createFont(Font.PLAIN, Minecraft.getMinecraft().getResourceManager().getResource(resourceLocation).getInputStream());
            output = output.deriveFont(fontSize);
            return new UnicodeFontRenderer(output);
        } catch (Exception e) {
            return null;
        }
    }
}
