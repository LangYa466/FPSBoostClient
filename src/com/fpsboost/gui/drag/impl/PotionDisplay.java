package com.fpsboost.gui.drag.impl;

import com.fpsboost.Access;
import com.fpsboost.annotations.event.EventTarget;
import com.fpsboost.annotations.system.Module;
import com.fpsboost.events.render.Render2DEvent;
import com.fpsboost.gui.drag.Dragging;
import com.fpsboost.gui.font.FontManager;
import com.fpsboost.gui.font.UnicodeFontRenderer;
import com.fpsboost.module.Category;
import com.fpsboost.util.RenderUtil;
import com.fpsboost.util.render.ColorUtil;
import com.fpsboost.util.render.RoundedUtil;
import com.fpsboost.value.impl.BooleanValue;
import com.fpsboost.value.impl.ColorValue;
import com.fpsboost.value.impl.NumberValue;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.ArrayList;

@Module(name = "PotionDisplay",cnName = "药水显示",description = "显示你的药水效果",category = Category.GUI)
public class PotionDisplay implements Access.InstanceAccess {

    private final BooleanValue backgroundValue = new BooleanValue("背景",true);
    private final ColorValue colorValue = new ColorValue("背景颜色",new Color(0,0,0));
    private final NumberValue opacity = new NumberValue("背景不透明度", 0.25, 0.0, 1, .05);
    private final NumberValue backgroundRadiusValue = new NumberValue("背景圆角值", 2,0,10,1);

    private final UnicodeFontRenderer fontRenderer = FontManager.M22;
    private final UnicodeFontRenderer fontRenderer2 = FontManager.S22;
    private final Dragging drag = Access.getInstance().getDragManager().createDrag(this.getClass(), "PotionDisplay", 52, 52);
    private final ResourceLocation res = new ResourceLocation("textures/gui/container/inventory.png");

    @EventTarget
    public void onRender2D(Render2DEvent event) {
        ArrayList<PotionEffect> collection = new ArrayList<>(mc.thePlayer.getActivePotionEffects());

        float width = 0F;
        drag.setWidth(width);
        drag.setHeight(collection.size() * 30);
        if (!collection.isEmpty()) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(drag.getX(),drag.getY() - 30,0F);
            RenderUtil.resetColor();
// 不是 还差一个
            collection.sort((o1, o2) -> {
                String os1 = "";

                if (o1.getAmplifier() == 1) {
                    os1 = os1 + " " + I18n.format("enchantment.level.2");
                } else if (o1.getAmplifier() == 2) {
                    os1 = os1 + " " + I18n.format("enchantment.level.3");
                } else if (o1.getAmplifier() == 3) {
                    os1 = os1 + " " + I18n.format("enchantment.level.4");
                }

                String os2 = "";

                if (o2.getAmplifier() == 1) {
                    os2 = os2 + " " + I18n.format("enchantment.level.2");
                } else if (o2.getAmplifier() == 2) {
                    os2 = os2 + " " + I18n.format("enchantment.level.3");
                } else if (o2.getAmplifier() == 3) {
                    os2 = os2 + " " + I18n.format("enchantment.level.4");
                }
                return Integer.compare(fontRenderer.getStringWidth(Potion.getDurationString(o2) + os2), fontRenderer.getStringWidth(Potion.getDurationString(o1) + os1));
            });

            int count = 0;
            for (PotionEffect potioneffect : collection) {
                count++;
                GlStateManager.pushMatrix();
                GlStateManager.translate(0F,count * 30,0F);
                float allStringWidth;
                Potion potion = Potion.potionTypes[potioneffect.getPotionID()];

                String s1 = I18n.format(potion.getName());
                String s = Potion.getDurationString(potioneffect);

                if (potioneffect.getAmplifier() == 1) {
                    s1 = s1 + " " + I18n.format("enchantment.level.2");
                } else if (potioneffect.getAmplifier() == 2) {
                    s1 = s1 + " " + I18n.format("enchantment.level.3");
                } else if (potioneffect.getAmplifier() == 3) {
                    s1 = s1 + " " + I18n.format("enchantment.level.4");
                }
                // 不管了一会再说

                allStringWidth = fontRenderer.getStringWidth(s1) + fontRenderer2.getStringWidth(s);
                if (allStringWidth > width) {
                    width = allStringWidth;
                    drag.setWidth(width);
                }

                Color color = ColorUtil.applyOpacity(colorValue.getValue(),opacity.getValue().floatValue());
                if (backgroundValue.getValue()) RoundedUtil.drawRound(0,0,allStringWidth + 10F,25F,backgroundRadiusValue.getValue().floatValue(),color);

                // draw potion name with i18n
                fontRenderer.drawStringWithShadow(s1, 25,3, -1);
                // draw potion duration
                fontRenderer2.drawStringWithShadow(s, 25, 15, -1);

                RenderUtil.resetColor();
                if (potion.hasStatusIcon()) {
                    int i1 = potion.getStatusIconIndex();
                    mc.getTextureManager().bindTexture(res);
                    Gui.drawTexturedModalRect2(4,4.5F, i1 % 8 * 18, 198 + i1 / 8 * 18, 18, 18);
                }

                GlStateManager.popMatrix();
            }
// 因为我写了个potiondisplay 然后我sort的时候发现他那个String长度有问题然后我debug了一下发现他没有把等级那个LV多少的计算进去所以sort就会有问题
            GlStateManager.popMatrix();
        }
    }
}
