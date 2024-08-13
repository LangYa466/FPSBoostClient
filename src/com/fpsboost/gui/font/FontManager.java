package com.fpsboost.gui.font;

import java.awt.Font;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class FontManager {
    private static final Font MFont = getFont(new ResourceLocation("client/fonts/HarmonyOS_Sans_SC_Regular.ttf"));

    public static UnicodeFontRenderer M14 = getMFont(14);
    public static UnicodeFontRenderer M16 = getMFont(16);
    public static UnicodeFontRenderer M18 = getMFont(18);
    public static UnicodeFontRenderer M22 = getMFont(22);
    public static UnicodeFontRenderer M50 = getMFont(50);

    private static Font getFont(ResourceLocation resourceLocation) {
        try {
            return Font.createFont(Font.PLAIN, Minecraft.getMinecraft().getResourceManager().getResource(resourceLocation).getInputStream());
        } catch (Exception e) {
            return null;
        }
    }
    private static UnicodeFontRenderer getMFont(float fontSize) {
        try {
            Font setSizeFont = MFont.deriveFont(fontSize);
            return new UnicodeFontRenderer(setSizeFont);
        } catch (Exception e) {
            return null;
        }
    }
}
