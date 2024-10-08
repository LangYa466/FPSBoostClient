package com.fpsboost.module.boost;

import com.fpsboost.annotations.event.EventTarget;
import com.fpsboost.annotations.system.Module;
import com.fpsboost.events.render.Render3DEvent;
import com.fpsboost.module.Category;
import com.fpsboost.util.render.ColorUtil;
import com.fpsboost.value.impl.BooleanValue;
import com.fpsboost.value.impl.NumberValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;

@Module(name = "DragonWings",cnName = "龙翅膀",description = "在你背后添加末影龙的翅膀",category = Category.Boost)
public class DragonWings extends ModelBase {
	private final Minecraft mc;
	public static ResourceLocation location = new ResourceLocation("client/wings.png");
	private final ModelRenderer wing;
	private final ModelRenderer wingTip;
	private final boolean playerUsesFullHeight;
	public BooleanValue fullHeight = new BooleanValue("自适应高度", false);
	private final BooleanValue rainbowValue = new BooleanValue("彩虹色",false);
	public final NumberValue scaleValue = new NumberValue("大小", 100.0, 0.0, 100.0, 1.0);

	public DragonWings() {
		this.mc = Minecraft.getMinecraft();
		this.playerUsesFullHeight = fullHeight.getValue();

		// Set texture offsets.
		setTextureOffset("wing.bone", 0, 0);
		setTextureOffset("wing.skin", -10, 8);
		setTextureOffset("wingtip.bone", 0, 5);
		setTextureOffset("wingtip.skin", -10, 18);

		// Create wing model renderer.
		wing = new ModelRenderer(this, "wing");
		wing.setTextureSize(30, 30); // 300px / 10px
		wing.setRotationPoint(-2F, 0, 0);
		wing.addBox("bone", -10.0F, -1.0F, -1.0F, 10, 2, 2);
		wing.addBox("skin", -10.0F, 0.0F, 0.5F, 10, 0, 10);

		// Create wing tip model renderer.
		wingTip = new ModelRenderer(this, "wingtip");
		wingTip.setTextureSize(30, 30); // 300px / 10px
		wingTip.setRotationPoint(-10.0F, 0.0F, 0.0F);
		wingTip.addBox("bone", -10.0F, -0.5F, -0.5F, 10, 1, 1);
		wingTip.addBox("skin", -10.0F, 0.0F, 0.5F, 10, 0, 10);
		wing.addChild(wingTip); // Make the wingtip rotate around the wing.
	}

	@EventTarget
	public void onRenderPlayer(Render3DEvent event)
	{
		EntityPlayer player = mc.thePlayer;

		if ( !player.isInvisible() && mc.gameSettings.thirdPersonView > 0) // Should render wings onto this player?
		{
			renderWings(player, event.getRenderPartialTicks(),location);
		}
	}

	private void renderWings(EntityPlayer player, float partialTicks,ResourceLocation location)
	{
		double scale = scaleValue.getValue() / 100D;
		double rotate = interpolate(player.prevRenderYawOffset, player.renderYawOffset, partialTicks);

		GL11.glPushMatrix();
		GL11.glScaled(-scale, -scale, scale);
		GL11.glRotated(180 + rotate, 0, 1, 0); // Rotate the wings to be with the player.
		GL11.glTranslated(0, -(playerUsesFullHeight ? 1.45 : 1.25) / scale, 0); // Move wings correct amount up.
		GL11.glTranslated(0, 0, 0.2 / scale);

		if (player.isSneaking())
		{
			GL11.glTranslated(0D, 0.125D / scale, 0D);
		}

		if (rainbowValue.getValue()) {
			float[] colors = getColors();
			GL11.glColor3f(colors[0], colors[1], colors[2]);
		}
		mc.getTextureManager().bindTexture(location);

		for (int j = 0; j < 2; ++j)
		{
			GL11.glEnable(GL11.GL_CULL_FACE);
			float f11 = (System.currentTimeMillis() % 1000) / 1000F * (float) Math.PI * 2.0F;
			this.wing.rotateAngleX = -1.3962634015954636f - (float) Math.cos(f11) * 0.2F;
			this.wing.rotateAngleY = 0.3490658503988659f + (float) Math.sin(f11) * 0.4F;
			this.wing.rotateAngleZ = 0.3490658503988659f;
			this.wingTip.rotateAngleZ = -((float)(Math.sin((f11 + 2.0F)) + 0.5D)) * 0.75F;
			this.wing.render(0.0625F);
			GL11.glScalef(-1.0F, 1.0F, 1.0F);

			if (j == 0)
			{
				GL11.glCullFace(1028);
			}
		}

		GL11.glCullFace(1029);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glColor3f(255F, 255F, 255F);
		GL11.glPopMatrix();
	}

	private float interpolate(float yaw1, float yaw2, float percent)
	{
		float f = (yaw1 + (yaw2 - yaw1) * percent) % 360;

		if (f < 0)
		{
			f += 360;
		}

		return f;
	}

	public float[] getColors()
	{
		Color color = ColorUtil.rainbow();
		return new float[] {color.getRed(), color.getGreen(), color.getBlue(),150F};
	}

}