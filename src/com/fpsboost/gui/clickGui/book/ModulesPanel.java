package com.fpsboost.gui.clickGui.book;

import com.fpsboost.Access;
import com.fpsboost.gui.clickGui.book.components.ModuleRect;
import com.fpsboost.gui.font.FontManager;
import com.fpsboost.module.Category;
import com.fpsboost.module.handlers.ModuleHandle;
import com.fpsboost.module.render.ClickGui;
import com.fpsboost.util.HoveringUtil;
import com.fpsboost.util.MathUtils;
import com.fpsboost.util.RenderUtil;
import com.fpsboost.util.animations.Animation;
import com.fpsboost.util.animations.Direction;
import com.fpsboost.util.animations.impl.DecelerateAnimation;
import com.fpsboost.util.render.RoundedUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ModulesPanel extends Panel {

    public List<ModuleRect> modules = new ArrayList<>();
    public Animation expandAnim;
    public Animation expandAnim2;
    Scroll settingScroll = new Scroll();
    private HashMap<ModuleHandle, SettingsPanel> settingsPanelHashMap;
    private HashMap<Category, Scroll> scrollHashMap;
    private boolean rightClicked = false;
    public Category currentCategory;
    private boolean typing = false;
    private ModuleRect currentlySelected;

    public boolean isTyping() {
        return typing;
    }

    @Override
    public void initGui() {
        expandAnim2 = new DecelerateAnimation(300, 1, Direction.FORWARDS);
        expandAnim2.setDirection(Direction.BACKWARDS);
        if (scrollHashMap == null) {
            scrollHashMap = new HashMap<>();
            for (Category category : Category.values()) {
                scrollHashMap.put(category, new Scroll());
            }
            //this is so damn aids but it just might work
            scrollHashMap.put(null, new Scroll());
        }

        refreshSettingMap();

    }

    public void refreshSettingMap() {
        if (settingsPanelHashMap == null) {
            settingsPanelHashMap = new HashMap<>();
            for (ModuleHandle module : Access.getInstance().getModuleManager().getCModules()) {
                SettingsPanel settingsPanel = new SettingsPanel(module);
                settingsPanel.initGui();
                settingsPanelHashMap.put(module, settingsPanel);
            }
        } else {
            settingsPanelHashMap.forEach((m, p) -> p.initGui());
        }
    }


    @Override
    public void keyTyped(char typedChar, int keyCode) {
        modules.forEach(moduleRect -> moduleRect.keyTyped(typedChar, keyCode));

        if (currentlySelected != null) {
            settingsPanelHashMap.get(currentlySelected.module).keyTyped(typedChar, keyCode);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        typing = false;

        expandAnim2.setDirection(rightClicked ? Direction.FORWARDS : Direction.BACKWARDS);
        int spacing = 0;
        Scroll scroll = scrollHashMap.get(currentCategory);

        if (!(HoveringUtil.isHovering(x + (250 + (55 * expandAnim.getOutput().floatValue())), y, 135, 250, mouseX, mouseY) && currentlySelected != null)) {
            scroll.onScroll(25);
        }
        double scrollVal = MathUtils.roundToHalf(scroll.getScroll());

        FontManager.M18.drawCenteredString("按这里就可以拖动", x + 305 / 2f, (float) (y - 15 + scrollVal),
                new Color(128, 134, 141, 150).getRGB());


        scroll.setMaxScroll(Math.max(0, (modules.size() - 4) * 50));
        for (ModuleRect module : modules) {
            module.rectWidth = (float) (305);
            module.x = this.x;
            module.y = (float) (this.y + spacing + scrollVal);
            module.bigRecty = this.bigRecty;

            if (!typing) {
                typing = module.binding != null;
            }

            module.drawSettingThing = currentlySelected == module;

            module.drawScreen(mouseX, mouseY);
            spacing += 50;


            if (module.rightClicked) {
                if (currentlySelected == module) {
                    rightClicked = false;
                    currentlySelected = null;
                    module.rightClicked = false;
                    continue;
                } else {
                    rightClicked = true;
                }
                settingScroll = new Scroll();
                currentlySelected = module;
                module.rightClicked = false;
            }
        }


        if (currentlySelected != null) {
            if (HoveringUtil.isHovering(x + (float) (305), y, 135, 250, mouseX, mouseY)) {
                settingScroll.onScroll(25);
            }

            //  settingScroll.setMaxScroll(Math.max(0,(Mo - 4) * 50));
            float newX = x + 5 + (float) (305);
            RoundedUtil.drawRound(newX, y - 20, 130, 255, 8, new Color(47, 49, 54));
            //       RenderUtil.renderRoundedRect(newX, y - 15, 130, 255, 10, new Color(47, 49, 54).getRGB());
            RenderUtil.setAlphaLimit(0);
            Gui.drawGradientRect2(newX - .5f, y, 130.5f, 8, new Color(0, 0, 0, 70).getRGB(), new Color(0, 0, 0, 0).getRGB());
            FontManager.M22.drawCenteredString(currentlySelected.module.getName(), newX + 125 / 2f, y - 15, -1);

            // 获取当前屏幕分辨率
            int screenWidth = Minecraft.getMinecraft().displayWidth;
            int screenHeight = Minecraft.getMinecraft().displayHeight;

            // 确定绘制区域的原始坐标和尺寸
            float originalX = x + 305;
            float originalY = y - 20;
            float originalWidth = 135;
            float originalHeight = 255;

            // 将原始坐标和尺寸映射到屏幕分辨率
            // 注意：这里的宽度和高度需要根据屏幕的实际 DPI 设置进行调整
            // 获取默认屏幕设备
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice gd = ge.getDefaultScreenDevice();

            // 获取屏幕尺寸
            Dimension screenSize = gd.getDefaultConfiguration().getBounds().getSize();

            // 获取屏幕宽度和高度
            int screenWidth2 = screenSize.width;
            int screenHeight2 = screenSize.height;

            float scaleX = (float) screenWidth / screenWidth2;
            float scaleY = (float) screenHeight / screenHeight2;

            float adjustedX = originalX * scaleX;
            float adjustedY = originalY * scaleY;
            float adjustedWidth = originalWidth * scaleX;
            float adjustedHeight = originalHeight * scaleY;

            // 启用 scissor 测试
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            RenderUtil.scissor(adjustedX, adjustedY, adjustedWidth, adjustedHeight);

            // 绘制界面内容
            SettingsPanel settingsPanel = settingsPanelHashMap.get(currentlySelected.module);
            settingScroll.setMaxScroll(Math.max(0, settingsPanel.maxScroll - 100));
            settingsPanel.x = x + 305;
            settingsPanel.y = (float) (y - 20 + MathUtils.roundToHalf(settingScroll.getScroll()));
            settingsPanel.drawScreen(mouseX, mouseY);

            if (!typing) {
                typing = settingsPanel.typing;
            }
            // 取消 scissor 测试
            GL11.glDisable(GL11.GL_SCISSOR_TEST);

        }

        ((ModernClickGui)ClickGui.modernClickGui).adjustWidth((125 * expandAnim2.getOutput().floatValue()));
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        modules.forEach(moduleRect -> moduleRect.mouseClicked(mouseX, mouseY, button));
        if (currentlySelected != null) {
            settingsPanelHashMap.get(currentlySelected.module).mouseClicked(mouseX, mouseY, button);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        modules.forEach(moduleRect -> moduleRect.mouseReleased(mouseX, mouseY, state));
        if (currentlySelected != null) {
            settingsPanelHashMap.get(currentlySelected.module).mouseReleased(mouseX, mouseY, state);
        }
    }
}