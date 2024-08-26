package com.fpsboost.gui.drag.impl;

import com.fpsboost.annotations.system.Module;
import com.fpsboost.gui.drag.Dragging;
import com.fpsboost.module.Category;
import net.minecraft.client.gui.GuiChat;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import com.fpsboost.Access;
import com.fpsboost.annotations.event.EventTarget;
import com.fpsboost.events.render.Render2DEvent;
import com.fpsboost.value.impl.BooleanValue;
import com.fpsboost.value.impl.ComboValue;

@Module(name = "ArmorDisplay",cnName = "装备显示",description = "显示你的装备",category = Category.GUI)
public class ArmorDisplay implements Access.InstanceAccess {

    private final BooleanValue heldItem = new BooleanValue("显示手持物品",true);
    private final ComboValue mode = new ComboValue("显示方向","横","横","竖");

    private final Dragging drag = Access.getInstance().getDragManager().createDrag(this.getClass(), "armorStatus", 150, 150);

	@EventTarget
	public void onRender2D(Render2DEvent event) {
		
        ScaledResolution sr = new ScaledResolution(mc);
        boolean horizontal = mode.isMode("横");
        
        int x;
        int y;
        int addHeldItem = heldItem.getValue() ? 16 : 0;

        ItemStack sword = new ItemStack(Items.diamond_sword);

        if(mode.isMode("横")) {
        	x = 65 + addHeldItem;
        	y = 16;
        } else {
        	x = 16;
        	y = 65 + addHeldItem;
        }

        for (int i21 = 0; i21 < mc.thePlayer.inventory.armorInventory.length; ++i21) {
        	
            final ItemStack is = mc.thePlayer.inventory.armorInventory[i21];
            
            if(mc.currentScreen instanceof GuiChat) {
            	this.renderFakeArmorStatus();
            }else {
                this.renderArmorStatus(sr, i21, is);
            }
        }
        
        if(heldItem.getValue()) {
            GL11.glPushMatrix();
            RenderHelper.enableGUIStandardItemLighting();

            if (mc.currentScreen instanceof GuiChat) {
                mc.getRenderItem().renderItemAndEffectIntoGUI(sword, (int) (drag.getXPos() + (horizontal ? (-16 * -1 + 48) : 0)), (int) (drag.getYPos() + (horizontal ? 0 : (-16 * -1 + 48))));
            } else {
                mc.getRenderItem().renderItemAndEffectIntoGUI(mc.thePlayer.getHeldItem(), (int) (drag.getXPos() + (horizontal ? (-16 * -1 + 48) : 0)), (int) (drag.getYPos() + (horizontal ? 0 : (-16 * -1 + 48))));
            }

            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableLighting();
            GL11.glPopMatrix();
        }

        drag.setWidth(x);
        drag.setHeight(y);
	}
	
	private void renderFakeArmorStatus() {

        boolean horizontal;
        
        ItemStack helmet = new ItemStack(Items.diamond_helmet);
        ItemStack chestplate = new ItemStack(Items.diamond_chestplate);
        ItemStack leggings = new ItemStack(Items.diamond_leggings);
        ItemStack boots = new ItemStack(Items.diamond_boots);

        horizontal = mode.isMode("横");
        
        mc.getRenderItem().renderItemAndEffectIntoGUI(helmet, (int) (drag.getXPos() + (horizontal ? -16 * 3 + 48 : 0)), (int) (drag.getYPos() + (horizontal ? 0 : -16 * 3 + 48)));
        mc.getRenderItem().renderItemAndEffectIntoGUI(chestplate, (int) (drag.getXPos() + (horizontal ? -16 * 2 + 48 : 0)), (int) (drag.getYPos() + (horizontal ? 0 : -16 * 2 + 48)));
        mc.getRenderItem().renderItemAndEffectIntoGUI(leggings, (int) (drag.getXPos() + (horizontal ? -16 * 1 + 48 : 0)), (int) (drag.getYPos() + (horizontal ? 0 : -16 * 1 + 48)));
        mc.getRenderItem().renderItemAndEffectIntoGUI(boots, (int) (drag.getXPos() + (horizontal ? -16 * 0 + 48 : 0)), (int) (drag.getYPos() + (horizontal ? 0 : -16 * 0 + 48)));
	}

    private void renderArmorStatus(final ScaledResolution sr, final int pos, final ItemStack itemStack) {


        if (itemStack == null) {
            return;
        }

        int posXAdd;
        int posYAdd;
        RenderItem itemRender = mc.getRenderItem();

        float prevZ = itemRender.zLevel;

        if(mode.isMode("横")) {
            posXAdd = -16 * pos + 48;
            posYAdd = 0;
        } else {
            posXAdd = 0;
            posYAdd = -16 * pos + 48;
        }

        mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, (int) (drag.getXPos() + posXAdd), (int) (drag.getYPos() + posYAdd));
    }
}