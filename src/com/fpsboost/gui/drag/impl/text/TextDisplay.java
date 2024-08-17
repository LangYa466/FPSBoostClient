package com.fpsboost.gui.drag.impl.text;

import com.fpsboost.Access;
import com.fpsboost.gui.drag.Dragging;
import com.fpsboost.gui.font.FontManager;
import com.fpsboost.gui.font.UnicodeFontRenderer;
import com.fpsboost.util.render.ColorUtil;
import com.fpsboost.util.render.RoundedUtil;

import java.awt.*;

public class TextDisplay implements Access.InstanceAccess {
    private UnicodeFontRenderer fontRenderer = FontManager.S22;

    private final Dragging drag;
    public TextDisplay(String name) {
        this.drag = Access.getInstance().getDragManager().createDrag(this.getClass(), name, 33, 33);
    }

    public TextDisplay(String name,UnicodeFontRenderer fontRenderer) {
        this.fontRenderer = fontRenderer;
        this.drag = Access.getInstance().getDragManager().createDrag(this.getClass(), name, 33, 33);
    }
    public void draw(String text,boolean bg,float opacity,float radius) {
        float x = drag.getXPos();
        float y = drag.getYPos();
        Color color = ColorUtil.applyOpacity(Color.BLACK,opacity);
        if (bg) RoundedUtil.drawRound(x,y,fontRenderer.getStringWidth(text) + 10.5F,fontRenderer.getHeight(),radius,color);
        drag.setWH(fontRenderer.getStringWidth(text)  + 10.5F,fontRenderer.getHeight());
        fontRenderer.drawStringWithShadow(text, x + 5, y + 3,-1);
    }
    public void draw(String text,boolean bg,Color color,float opacity,float radius) {
        float x = drag.getXPos();
        float y = drag.getYPos();
        Color alphaColor = ColorUtil.applyOpacity(color,opacity);
        if (bg) RoundedUtil.drawRound(x,y,fontRenderer.getStringWidth(text) + 10.5F,fontRenderer.getHeight(),radius,alphaColor);
        drag.setWH(fontRenderer.getStringWidth(text)  + 10.5F,fontRenderer.getHeight());
        fontRenderer.drawStringWithShadow(text, x + 5, y + 3,-1);
    }
}
