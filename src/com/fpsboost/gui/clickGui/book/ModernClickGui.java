package com.fpsboost.gui.clickGui.book;

import com.fpsboost.gui.clickGui.book.components.CategoryButton;
import com.fpsboost.gui.clickGui.book.components.ClickCircle;
import com.fpsboost.gui.clickGui.book.components.ModuleRect;
import com.fpsboost.gui.clickGui.book.components.Component;
import com.fpsboost.Access;
import com.fpsboost.gui.drag.Drag;
import com.fpsboost.gui.font.FontManager;
import com.fpsboost.module.Category;
import com.fpsboost.module.handlers.ModuleHandle;
import com.fpsboost.util.HoveringUtil;
import com.fpsboost.util.animations.Animation;
import com.fpsboost.util.animations.Direction;
import com.fpsboost.util.animations.impl.DecelerateAnimation;
import com.fpsboost.util.render.RoundedUtil;
import com.fpsboost.util.render.StencilUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;
import java.util.*;

public class ModernClickGui extends GuiScreen {

    public static final Drag drag = new Drag(40, 40);
    public static boolean searching = false;
    private final Color backgroundColor = new Color(30, 31, 35);
    private final Color categoryColor = new Color(47, 49, 54);
    private final Color lighterGray = new Color(68, 71, 78);
    private final List<ClickCircle> circleClicks = new ArrayList<>();
    private final List<Component> categories = new ArrayList() {{
        for (Category category : Category.values()) {
            add(new CategoryButton(category));
        }
    }};
    public float rectHeight = 255, rectWidth = 370;
    private Category currentCategory = Category.Boost;
    private Animation openingAnimation;
    private Animation expandedAnimation;
    private ModulesPanel modpanel;
    private HashMap<Category, ArrayList<ModuleRect>> moduleRects;
    private boolean firstOpen = true;
    public boolean typing;

    public void drawBigRect() {
        float x = drag.getX(), y = drag.getY();

        if (!openingAnimation.isDone()) {
            x -= width + rectWidth / 2f;
            x += (width + rectWidth / 2f) * openingAnimation.getOutput().floatValue();
        } else if (openingAnimation.getDirection().equals(Direction.BACKWARDS)) {
            return;
        }

        RoundedUtil.drawRound(x, y, rectWidth, rectHeight, 10, Color.BLACK);
    }

    @Override
    public void onDrag(int mouseX, int mouseY) {
        if (firstOpen) {
            drag.setX(width / 2F - rectWidth / 2F);
            drag.setY(height / 2F - rectHeight / 2F);
            firstOpen = false;
        }

        drag.onDraw(mouseX, mouseY);
    }

    @Override
    public void initGui() {
        if (firstOpen) {
            drag.setX(width / 2F - rectWidth / 2F);
            drag.setY(height / 2F - rectHeight / 2F);
            firstOpen = false;
        }
        if (modpanel == null) {
            modpanel = new ModulesPanel();
        }

        currentCategory = Category.Boost;
        categories.forEach(Component::initGui);
        openingAnimation = new DecelerateAnimation(300, 1);
        expandedAnimation = new DecelerateAnimation(250, 1);

        if (moduleRects != null) {
            moduleRects.forEach((cat, list) -> list.forEach(ModuleRect::initGui));
        }
        modpanel.initGui();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == 1 && !typing) {
            openingAnimation.setDirection(Direction.BACKWARDS);
        }

        modpanel.keyTyped(typedChar, keyCode);
    }

    private float adjustment = 0;
    private final List<ModuleRect> searchResults = new ArrayList<>();

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (moduleRects == null) {
            moduleRects = new HashMap<>();
            for (Category category : Category.values()) {
                ArrayList<ModuleRect> modules = new ArrayList<>();
                for (ModuleHandle module : Access.getInstance().getModuleManager().getCModulesByCategory(category)) {
                    modules.add(new ModuleRect(module));
                }

                moduleRects.put(category, modules);
            }
            moduleRects.forEach((cat, list) -> list.forEach(ModuleRect::initGui));
            modpanel.refreshSettingMap();
            return;
        }

        typing = modpanel.isTyping();

        adjustment = 0;

        drag.onDraw(mouseX, mouseY);
        float x = drag.getX(), y = drag.getY();


        if (!openingAnimation.isDone()) {
            x -= width + rectWidth / 2f;
            x += (width + rectWidth / 2f) * openingAnimation.getOutput().floatValue();
        } else if (openingAnimation.getDirection().equals(Direction.BACKWARDS)) {
            mc.displayGuiScreen(null);
            return;
        }


        RoundedUtil.drawRound(x, y, rectWidth, rectHeight, 10, backgroundColor);

        float catWidth = (100 - (55 * expandedAnimation.getOutput().floatValue()));
        boolean hoveringCat = HoveringUtil.isHovering(x, y, catWidth, rectHeight, mouseX, mouseY);
        if (expandedAnimation.isDone()) {
            expandedAnimation.setDirection(hoveringCat && !searching ? Direction.BACKWARDS : Direction.FORWARDS);
        }


        RoundedUtil.drawRound(x, y, (float) (100 - (55 * expandedAnimation.getOutput().floatValue())), rectHeight, 10, categoryColor);
        //   RenderUtil.renderRoundedRect(x, y, (float) (100 - (55 * expandedAnimation.getOutput().floatValue())), rectHeight, 10, categoryColor.getRGB());


        adjustWidth(55 - (55 * expandedAnimation.getOutput().floatValue()));

        StencilUtil.initStencilToWrite();
        Gui.drawRect2(x, y, (float) (100 - (55 * expandedAnimation.getOutput().floatValue())), rectHeight, -1);
        StencilUtil.readStencilBuffer(1);


        GL11.glEnable(GL11.GL_BLEND);
        mc.getTextureManager().bindTexture(new ResourceLocation("client/logo.png"));
        Gui.drawModalRectWithCustomSizedTexture((float) (x + 9 + (3 * expandedAnimation.getOutput().floatValue())), y + 6, 0, 0, 20.5f, 20.5f, 20.5f, 20.5f);
        GL11.glDisable(GL11.GL_BLEND);

        Gui.drawRect2(x + 10, y + 35, 80 - (55 * expandedAnimation.getOutput().floatValue()), 1, lighterGray.getRGB());


        float xAdjust = 10 * expandedAnimation.getOutput().floatValue();
        FontManager.M20.drawString("FPSBoost", x + 35 + xAdjust, y + 13, -1);

        FontManager.M14.drawString(Access.CLIENT_VERSION, x + 41 + FontManager.M18.getStringWidth("FPSBoost") + xAdjust, y + 15.5f,
                new Color(98, 98, 98).getRGB());


        int spacing = 0;
        for (Component category : categories) {
            category.x = x + 8 + (4 * expandedAnimation.getOutput().floatValue());
            category.y = y + 50 + spacing;
            CategoryButton currentCatego = ((CategoryButton) category);
            currentCatego.expandAnimation = expandedAnimation;
            currentCatego.currentCategory = currentCategory;
            category.drawScreen(mouseX, mouseY);
            spacing += 30;
        }

        StencilUtil.uninitStencilBuffer();


        float recWidth = 100 - (55 * expandedAnimation.getOutput().floatValue());
        StencilUtil.initStencilToWrite();
        RoundedUtil.drawRound(x, y, rectWidth, rectHeight, 10, backgroundColor);
        StencilUtil.readStencilBuffer(1);

        /*+ ((rectWidth - 50) * searchingAnimation.getOutput().floatValue())*/
        modpanel.x = x + recWidth + 10;
        modpanel.y = y + 20;
        modpanel.bigRecty = y;
        modpanel.modules = getModuleRects(currentCategory);
        modpanel.currentCategory = currentCategory;
        modpanel.expandAnim = expandedAnimation;
        modpanel.drawScreen(mouseX, mouseY);

        StencilUtil.uninitStencilBuffer();

        for (ClickCircle clickCircle : circleClicks) {
            clickCircle.drawScreen(mouseX, mouseY);
        }

        rectWidth = 370 + adjustment;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        float rectWidth = 400;
        double x = drag.getX(), y = drag.getY();
        final boolean canDrag = HoveringUtil.isHovering((float) x, (float) y, rectWidth, 20f, mouseX, mouseY);

        drag.onClick(mouseX, mouseY, mouseButton, canDrag);


        circleClicks.removeIf(clickCircle1 -> clickCircle1.fadeAnimation.isDone() && clickCircle1.fadeAnimation.getDirection().equals(Direction.BACKWARDS));
        ClickCircle clickCircle = new ClickCircle();
        clickCircle.x = mouseX;
        clickCircle.y = mouseY;
        circleClicks.add(clickCircle);


        for (Component category : categories) {
            category.mouseClicked(mouseX, mouseY, mouseButton);
            if (category.hovering) {
                currentCategory = ((CategoryButton) category).category;
                return;
            }
        }
        modpanel.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        drag.onRelease(state);
        modpanel.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }


    public void adjustWidth(float adjustment) {
        this.adjustment += adjustment;
    }

    public List<ModuleRect> getModuleRects(Category category) {
        return moduleRects.get(category);

    }

}