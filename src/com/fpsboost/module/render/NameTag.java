package com.fpsboost.module.render;

import com.fpsboost.Access;
import com.fpsboost.annotations.event.EventTarget;
import com.fpsboost.annotations.system.Module;
import com.fpsboost.events.render.Render3DEvent;
import com.fpsboost.events.render.RenderNameTagEvent;
import com.fpsboost.gui.font.FontManager;
import com.fpsboost.gui.font.UnicodeFontRenderer;
import com.fpsboost.module.Category;
import com.fpsboost.util.RankUtil;
import com.fpsboost.util.RenderUtil;
import com.fpsboost.util.render.RoundedUtil;
import com.fpsboost.value.impl.BooleanValue;
import com.fpsboost.value.impl.NumberValue;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Timer;
import org.lwjgl.opengl.GL11;

import java.awt.*;

@Module(name = "NameTag",description = "合法的NameTag 就是美化了原版的显示", category = Category.GUI)
public class NameTag implements Access.InstanceAccess {

    private final BooleanValue healthValue = new BooleanValue("血量", true);
    private final BooleanValue pingValue = new BooleanValue("延迟", true);
    private final BooleanValue distanceValue = new BooleanValue("距离", false);
    private final BooleanValue armorValue = new BooleanValue("装备", true);
    private final NumberValue scaleValue = new NumberValue("大小", 1F, 1F, 4F,0.5F);

    @EventTarget
    public void onRenderNameTag(RenderNameTagEvent event) {
        event.setCancelled(true);
    }

    @EventTarget
    public void onRender3D(Render3DEvent event) {
        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (!RenderUtil.isInView(entity) || !(entity instanceof EntityPlayer))
                continue;

            String text = entity.getDisplayName().getUnformattedText();

            if (entity == mc.thePlayer) {
                if (mc.gameSettings.thirdPersonView > 0) renderNameTag((EntityPlayer) entity, text);
            } else {
                renderNameTag((EntityPlayer) entity, text);
            }
        }
        // Reset color
        GlStateManager.resetColor();
    }

    public static int getPing(final EntityPlayer entityPlayer) {
        if (entityPlayer == null)
            return 0;
        final NetworkPlayerInfo networkPlayerInfo = mc.getNetHandler().getPlayerInfo(entityPlayer.getUniqueID());
        return networkPlayerInfo == null ? 0 : networkPlayerInfo.getResponseTime();
    }

    private void renderNameTag(EntityLivingBase entity, String tag) {
        UnicodeFontRenderer fontRenderer = FontManager.M22;

        // Modify tag
        String nameColor = entity.isInvisible() ? "§6" : entity.isSneaking() ? "§4" : "§7";
        int ping = entity instanceof EntityPlayer ? getPing((EntityPlayer) entity) : 0;

        String distanceText = distanceValue.getValue() ? "§7"+Math.round(mc.thePlayer.getDistanceToEntity(entity))+"m " : "";
        String pingText = pingValue.getValue() && entity instanceof EntityPlayer ? (ping > 200 ? "§c" : ping > 100 ? "§e" : "§a") + ping + "ms §7" : "";
        String healthText = healthValue.getValue() ? "§7§c " + Math.round(entity.getHealth()) + " HP" : "";

        String text = distanceText + pingText + nameColor + tag + healthText;

        // Push
        GL11.glPushMatrix();

        // Translate to player position
        Timer timer = mc.timer;
        RenderManager renderManager = mc.getRenderManager();


        GL11.glTranslated( // Translate to player position with render pos and interpolate it
                entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * timer.renderPartialTicks - renderManager.renderPosX,
                entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * timer.renderPartialTicks - renderManager.renderPosY + entity.getEyeHeight() + 0.55,
                entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * timer.renderPartialTicks - renderManager.renderPosZ
        );

        GL11.glRotatef(-renderManager.playerViewY, 0F, 1F, 0F);
        GL11.glRotatef(renderManager.playerViewX, 1F, 0F, 0F);


        // Scale
        float distance = mc.thePlayer.getDistanceToEntity(entity) * 0.25f;

        if (distance < 1F)
            distance = 1F;

        float scale = distance / 100f * scaleValue.getValue().floatValue();

        GL11.glScalef(-scale, -scale, scale);

        // Draw NameTag
        float width = fontRenderer.getStringWidth(text) * 0.5f;

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);

        RoundedUtil.drawRound(-width - 2F, -2F, width * 2 + 4F, fontRenderer.getHeight() + 2F,2, new Color(0,0,0,80));

        GL11.glEnable(GL11.GL_TEXTURE_2D);

     //   fontRenderer.drawString(text, 1F - width, fontRenderer == Fonts.minecraftFont ? 1F : 1.5F, 0xFFFFFF, true);
        fontRenderer.drawString(text, 1F - width, 1F, -1);

        if (armorValue.getValue() && entity instanceof EntityPlayer) {
            for (int index = 0; index < 4; index++) {
                if (entity.getEquipmentInSlot(index) == null)
                    continue;

                mc.getRenderItem().zLevel = -147F;
                mc.getRenderItem().renderItemAndEffectIntoGUI(entity.getEquipmentInSlot(index), -50 + index * 20, -22);
            }

            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
            GlStateManager.enableTexture2D();
        }

        // Pop
        GL11.glPopMatrix();
    }
}
