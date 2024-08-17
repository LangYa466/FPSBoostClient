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

            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            RenderUtil.scissor(newX, y + .5f, 135, 255);

            SettingsPanel settingsPanel = settingsPanelHashMap.get(currentlySelected.module);
            settingScroll.setMaxScroll(Math.max(0, settingsPanel.maxScroll - 100));
            settingsPanel.x = x + 305;
            settingsPanel.y = (float) (y - 20 + MathUtils.roundToHalf(settingScroll.getScroll()));
            settingsPanel.drawScreen(mouseX, mouseY);

            if (!typing) {
                typing = settingsPanel.typing;
            }

            GL11.glDisable(GL11.GL_SCISSOR_TEST);
        }

        ClickGui.modernClickGui.adjustWidth((125 * expandAnim2.getOutput().floatValue()));
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