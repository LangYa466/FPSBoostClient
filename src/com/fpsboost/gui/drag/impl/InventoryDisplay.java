package com.fpsboost.gui.drag.impl;

import com.fpsboost.Access;
import com.fpsboost.annotations.event.EventTarget;
import com.fpsboost.annotations.system.Module;
import com.fpsboost.events.render.Render2DEvent;
import com.fpsboost.gui.drag.Dragging;
import com.fpsboost.gui.font.FontManager;
import com.fpsboost.module.Category;
import com.fpsboost.util.render.ColorUtil;
import com.fpsboost.util.render.RoundedUtil;
import com.fpsboost.value.impl.BooleanValue;
import com.fpsboost.value.impl.ColorValue;
import com.fpsboost.value.impl.ComboValue;
import com.fpsboost.value.impl.NumberValue;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;


import java.awt.*;

@Module(name = "InventoryDisplay",description = "显示你的背包里面有什么物品",category = Category.GUI)
public class InventoryDisplay implements Access.InstanceAccess{
    public BooleanValue textRender = new BooleanValue("提示显示", true);
    public ComboValue textMode = new ComboValue("提示显示语言", "中文", "英文", "中文");
    private final ColorValue colorValue = new ColorValue("背景颜色",new Color(0,0,0));
    private static final NumberValue customAlpha = new NumberValue("背景不透明度",80,0,255,5);
    private static final NumberValue customRadius = new NumberValue("背景圆角值", 2,0,10,1);
    private final Dragging drag = Access.getInstance().getDragManager().createDrag(this.getClass(), "inventoryDisplay", 150, 150);

    @EventTarget
	public void onRender2D(Render2DEvent event) {
        float startX = drag.getXPos() + 4;
        float startY = drag.getYPos() + 20;
        int curIndex = 0;
        
        RoundedUtil.drawRound(drag.getXPos(), drag.getYPos(), 185, 79,customRadius.getValue().intValue(), ColorUtil.applyOpacity(colorValue.getValue(),customAlpha.getValue().intValue()));
        RoundedUtil.drawRound(drag.getXPos(), drag.getYPos() + 16, 185, 1, 0, Color.white);

        if (textRender.getValue()) {
            if (textMode.isMode("中文")) {
                FontManager.M22.drawString("背包", drag.getXPos() + 4.5F, drag.getYPos() + 4.5F, -1);
            } else {
                FontManager.M22.drawString("Inventory", drag.getXPos() + 4.5F, drag.getYPos() + 4.5F, -1);
            }
        }

        for(int i = 9; i < 36; ++i) {
            ItemStack slot = mc.thePlayer.inventory.mainInventory[i];
            if(slot == null) {
                startX += 20;
                curIndex += 1;

                if(curIndex > 8) {
                    curIndex = 0;
                    startY += 20;
                    startX = drag.getXPos() + 4;
                }

                continue;
            }

            this.drawItemStack(slot, (int) startX, (int) startY);
            startX += 20;
            curIndex += 1;
            if(curIndex > 8) {
                curIndex = 0;
                startY += 20;
                startX = drag.getXPos() + 4;
            }
        }
        
        drag.setWidth(185);
        drag.setHeight(79);
	}

    private void drawItemStack(ItemStack stack, int x, int y) {
        GlStateManager.pushMatrix();
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.disableAlpha();
        GlStateManager.clear(256);
        mc.getRenderItem().zLevel = -150.0F;
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        mc.getRenderItem().renderItemIntoGUI(stack, x, y);
        mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, stack, x, y, null);
        mc.getRenderItem().zLevel = 0.0F;
        GlStateManager.enableAlpha();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.popMatrix();
    }
}