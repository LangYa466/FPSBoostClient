package com.fpsboost.gui.clickGui.drop;

import com.fpsboost.gui.clickGui.drop.component.Component;
import com.fpsboost.gui.clickGui.drop.component.Frame;
import com.fpsboost.gui.font.FontManager;
import net.minecraft.client.gui.GuiScreen;
import com.fpsboost.module.Category;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class ClickGuiScreen extends GuiScreen {

    public static ArrayList<Frame> frames;
    public static int color = new Color(62, 175, 255).getRGB();
    private final int infoColor = new Color(255,255,255,180).getRGB();
    public void init(){
        frames = new ArrayList<>();
        int frameX = 5;
        for (Category category : Category.values()) {
            Frame frame = new Frame(category);
            frame.setX(frameX);
            frames.add(frame);
            frameX += frame.getWidth() + 1;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // this.drawDefaultBackground();
        for (Frame frame : frames) {
            frame.renderFrame();
            frame.updatePosition(mouseX, mouseY);
            for (Component comp : frame.getComponents()) {
                comp.updateComponent(mouseX, mouseY);
            }
        }
        FontManager.M22.drawStringWithShadow("您当前使用的是兼容模式",width - 200,5,infoColor);
    }


    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        for (Frame frame : frames) {
            if (frame.isWithinHeader(mouseX, mouseY) && mouseButton == 0) {
                frame.setDrag(true);
                frame.dragX = mouseX - frame.getX();
                frame.dragY = mouseY - frame.getY();
            }
            if (frame.isWithinHeader(mouseX, mouseY) && mouseButton == 1) {
                frame.setOpen(!frame.isOpen());
            }
            if (frame.isOpen()) {
                if (!frame.getComponents().isEmpty()) {
                    for (Component component : frame.getComponents()) {
                        component.mouseClicked(mouseX, mouseY, mouseButton);
                    }
                }
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        for (Frame frame : frames) {
            if (frame.isOpen() && keyCode != 1) {
                if (!frame.getComponents().isEmpty()) {
                    for (Component component : frame.getComponents()) {
                        component.keyTyped(typedChar, keyCode);
                    }
                }
            }
        }
        if (keyCode == 1) {
            this.mc.displayGuiScreen(null);
        }
    }


    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        for (Frame frame : frames) {
            frame.setDrag(false);
        }
        for (Frame frame : frames) {
            if (frame.isOpen()) {
                if (!frame.getComponents().isEmpty()) {
                    for (Component component : frame.getComponents()) {
                        component.mouseReleased(mouseX, mouseY, state);
                    }
                }
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }
}
