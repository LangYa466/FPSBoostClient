package com.fpsboost.gui.clickGui.book.components;

import com.fpsboost.gui.clickGui.book.Pair;
import com.fpsboost.gui.font.FontManager;
import com.fpsboost.module.handlers.ModuleHandle;
import com.fpsboost.util.HoveringUtil;
import com.fpsboost.util.RenderUtil;
import com.fpsboost.util.animations.Animation;
import com.fpsboost.util.animations.Direction;
import com.fpsboost.util.animations.impl.DecelerateAnimation;
import com.fpsboost.util.animations.impl.ElasticAnimation;
import com.fpsboost.util.render.ColorUtil;
import com.fpsboost.util.render.RoundedUtil;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class ModuleRect extends Component {
    public final ModuleHandle module;
    public Animation settingAnimation;
    public ModuleHandle binding;
    public float rectOffset = 0;
    public boolean rightClicked = false;
    public boolean drawSettingThing = false;
    private Animation rectScaleAnimation;
    private Animation checkScaleAnimation;

    public ModuleRect(ModuleHandle module) {
        this.module = module;
    }

    @Override
    public void initGui() {
        settingAnimation = new DecelerateAnimation(400, 1);
        settingAnimation.setDirection(Direction.BACKWARDS);

        rectScaleAnimation = new DecelerateAnimation(250, 1);
        rectScaleAnimation.setDirection(Direction.BACKWARDS);

        checkScaleAnimation = new ElasticAnimation(550, 1, 3.8f, 2, false);
        checkScaleAnimation.setDirection(Direction.BACKWARDS);

    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (binding != null) {
            if (keyCode == Keyboard.KEY_SPACE || keyCode == Keyboard.KEY_ESCAPE || keyCode == Keyboard.KEY_DELETE)
                binding.setKey(Keyboard.KEY_NONE);
            else
                binding.setKey(keyCode);
            binding = null;
        }

    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        RoundedUtil.drawRound(x + .5f, y + .5f, rectWidth - 1, 34, 5, new Color(47, 49, 54));
        //  RenderUtil.renderRoundedRect(x, y, rectWidth, 35, 5, new Color(47, 49, 54).getRGB());

        rectScaleAnimation.setDirection(module.isEnabled() ? Direction.FORWARDS : Direction.BACKWARDS);
        checkScaleAnimation.setDirection(module.isEnabled() ? Direction.FORWARDS : Direction.BACKWARDS);
        checkScaleAnimation.setDuration(module.isEnabled() ? 550 : 250);

        Pair<Color, Color> colors = Pair.of(new Color(0x4A4DAC), new Color(0xFFFFFF));


        RoundedUtil.drawRound(x + .5f, y + .5f, 34, 34, 5, new Color(68, 71, 78));
        RenderUtil.drawGoodCircle(x + 35 / 2f, y + 35 / 2f, 5, new Color(47, 49, 54).getRGB());

        float rectScale = rectScaleAnimation.getOutput().floatValue();

        GL11.glPushMatrix();
        GL11.glTranslatef(x + 17, y + 17, 0);
        GL11.glScaled(rectScale, rectScale, 0);
        GL11.glTranslatef(-(x + 17), -(y + 17), 0);
        GL11.glEnable(GL11.GL_BLEND);
        //  mc.getTextureManager().bindTexture(new ResourceLocation("Tenacity/gradient.png"));
        //Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, 35, 35, 35, 35);
        float width = 35;
        float height = 35;
        RoundedUtil.drawGradientCornerLR(x + .5f, y + .5f, width - 1, height - 1, 5,
                ColorUtil.applyOpacity(colors.getFirst(), rectScale),
                ColorUtil.applyOpacity(colors.getSecond(), rectScale));
        GL11.glPopMatrix();

        float textX = (float) (x + (18));
        float textY = (float) (y + 18.5);
        GL11.glPushMatrix();
        GL11.glTranslatef(textX + 9, textY + 9, 0);
        GL11.glScaled(Math.max(0, checkScaleAnimation.getOutput().floatValue()), Math.max(0, checkScaleAnimation.getOutput().floatValue()), 0);
        GL11.glTranslatef(-(textX + 9), -(textY + 9), 0);

        // FontUtil.iconFont35.drawSmoothString(FontUtil.CHECKMARK, textX, textY, ColorUtil.applyOpacity(-1, (float) checkScaleAnimation.getOutput().floatValue()));
        GL11.glPopMatrix();

        GlStateManager.color(1, 1, 1, 1);
        float xStart =  (x + 55 + FontManager.M24.getStringWidth(module.getName()));
        float yVal = y + 35 / 2f - FontManager.M18.getHeight() / 2f;
        if (binding != module && module.getDescription() != "") {
            float descWidth = 305 - ((55 + FontManager.M24.getStringWidth(module.getName())) + 15);

            FontManager.M18.drawWrappedText(module.getDescription(), xStart, yVal, new Color(128, 134, 141, 255).getRGB(), descWidth, 3);
        } else if (binding == module) {
            FontManager.M18.drawString(
                    "Currently bound to " + Keyboard.getKeyName(module.getKey()),
                    xStart, yVal, new Color(128, 134, 141).getRGB());
        }


        FontManager.M24.drawString(module.getName(), x + 42, y + 35 / 2f - FontManager.M24.getHeight() / 2f, -1);
        settingAnimation.setDirection(drawSettingThing ? Direction.FORWARDS : Direction.BACKWARDS);


        int interpolateColorr = ColorUtil.interpolateColorsBackAndForth(40, 1, colors.getFirst(), colors.getSecond(), false).getRGB();

        RoundedUtil.drawRound(x + rectWidth - 14.5f, y + .5f, 14, 34, 5,
                ColorUtil.interpolateColorC(new Color(47, 49, 54), new Color(interpolateColorr), (float) settingAnimation.getOutput().floatValue()));

        RenderUtil.drawGoodCircle(x + rectWidth - 7.5, y + 7.5, 2.5f, -1);
        RenderUtil.drawGoodCircle(x + rectWidth - 7.5, y + 17.5, 2.5f, -1);
        RenderUtil.drawGoodCircle(x + rectWidth - 7.5, y + 27.5, 2.5f, -1);

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (HoveringUtil.isHovering(x, y, rectWidth, 35, mouseX, mouseY)) {
            if (y > (bigRecty + rectOffset) && y < bigRecty + 255) {
                if (button == 0) {
                    module.toggle();
                }
                if (button == 2) {
                    binding = module;
                }
                rightClicked = button == 1;
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int button) {
        if (button == 0) {
            binding = null;
        }
    }
}