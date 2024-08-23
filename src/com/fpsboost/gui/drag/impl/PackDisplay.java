package com.fpsboost.gui.drag.impl;

import java.awt.*;
import java.util.List;

import com.fpsboost.Access;
import com.fpsboost.annotations.event.EventTarget;
import com.fpsboost.annotations.system.Enable;
import com.fpsboost.annotations.system.Module;
import com.fpsboost.events.misc.SwitchTextureEvent;
import com.fpsboost.events.render.Render2DEvent;
import com.fpsboost.gui.drag.Dragging;
import com.fpsboost.gui.font.FontManager;
import com.fpsboost.gui.font.UnicodeFontRenderer;
import com.fpsboost.module.Category;
import com.fpsboost.util.render.ColorUtil;
import com.fpsboost.util.render.RoundedUtil;
import com.fpsboost.value.impl.BooleanValue;
import com.fpsboost.value.impl.ColorValue;
import com.fpsboost.value.impl.NumberValue;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.util.ResourceLocation;

@Module(name = "PackDisplay",cnName = "材质包显示",description = "显示你正在用的材质包",category = Category.GUI)
public class PackDisplay implements Access.InstanceAccess {

	private IResourcePack pack;
	private ResourceLocation currentPack;
	private final ResourcePackRepository resourcePackRepository = mc.getResourcePackRepository();
	private List<ResourcePackRepository.Entry> packs = resourcePackRepository.getRepositoryEntries();
	private final UnicodeFontRenderer fr = FontManager.M22;
	private final Dragging pos = Access.getInstance().getDragManager().createDrag(this.getClass(), "packDisplay", 150, 150);
	private final BooleanValue bg = new BooleanValue("背景",true);
	private final ColorValue colorValue = new ColorValue("背景颜色",new Color(0,0,0));
	private static final NumberValue customAlpha = new NumberValue("背景不透明度",80,0,255,5);
	private static final NumberValue customRadius = new NumberValue("背景圆角值", 2,0,10,1);
	@Enable
	public void onEnable() {
		this.loadTexture();
	}

	@EventTarget
	public void onRender2D(Render2DEvent event) {
		
		GlStateManager.pushMatrix();
		
		if(pack == null) {
			pack = this.getCurrentPack();
		}

		if (bg.getValue()) RoundedUtil.drawRound(pos.getXPos(), pos.getYPos(), (float) (46 + (fr.getStringWidth(this.convertNormalText(pack.getPackName())))), 38,customRadius.getValue().intValue(), ColorUtil.applyOpacity(colorValue.getValue(),customAlpha.getValue().intValue()));
		mc.getTextureManager().bindTexture(this.currentPack);
		RoundedUtil.drawRoundTextured(pos.getXPos() + 4.5F, pos.getYPos() + 4.5F, 29, 29, 4, 1.0F);
		
		fr.drawString(this.convertNormalText(pack.getPackName()), pos.getXPos() + 40, pos.getYPos() + (29 / 2), -1);
		
		GlStateManager.popMatrix();
		
		pos.setWidth(46 + fr.getStringWidth(this.convertNormalText(pack.getPackName())));
		pos.setHeight(38);
	}

	
	@EventTarget
	public void onSwitchTexture(SwitchTextureEvent event) {
		packs = resourcePackRepository.getRepositoryEntries();
		pack = this.getCurrentPack();
		this.loadTexture();
	}

	private String convertNormalText(String text) {
		StringBuilder sb = new StringBuilder(text);

		String[] patterns = {"\\u00a7" + "1", "\\u00a7" + "2", "\\u00a7" + "3", "\\u00a7" + "4",
				"\\u00a7" + "5", "\\u00a7" + "6", "\\u00a7" + "7", "\\u00a7" + "8",
				"\\u00a7" + "9", "\\u00a7" + "a", "\\u00a7" + "b", "\\u00a7" + "c",
				"\\u00a7" + "d", "\\u00a7" + "e", "\\u00a7" + "f", "\\u00a7" + "g",
				"\\u00a7" + "k", "\\u00a7" + "l", "\\u00a7" + "m", "\\u00a7" + "n",
				"\\u00a7" + "o", "\\u00a7" + "r"};

		for (String pattern : patterns) {
			int index;
			while ((index = sb.indexOf(pattern)) != -1) {
				sb.delete(index, index + pattern.length());
			}
		}

		int zipIndex;
		while ((zipIndex = sb.indexOf(".zip")) != -1) {
			sb.delete(zipIndex, zipIndex + ".zip".length());
		}

		return sb.toString();
	}


	private void loadTexture() {
		DynamicTexture dynamicTexture;
		try {
			dynamicTexture = new DynamicTexture(getCurrentPack().getPackImage());
		} catch (Exception e) {
			dynamicTexture = TextureUtil.missingTexture;
		}
		this.currentPack = mc.getTextureManager().getDynamicTextureLocation("texturepackicon", dynamicTexture);
	}
	
	private IResourcePack getCurrentPack() {
		if (packs.size() > 0) {
			final IResourcePack last = packs.get(packs.size() - 1).getResourcePack();
			return last;
		}
		return mc.mcDefaultResourcePack;
	}
}