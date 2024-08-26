package com.fpsboost.gui.screen;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.io.IOException;

/**
 * @author LangYa
 * @since 2024/8/25 14:39
 */
public class GuiWelcome extends GuiScreen {

    @Override
    public void initGui() {
        this.buttonList.add(new GuiButton(1,width / 2,height / 2,"继续"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawRect(0,0,width,height, Color.white.getRGB());
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 1) {
            mc.displayGuiScreen(new GuiMainMenu());
        }
    }
}
