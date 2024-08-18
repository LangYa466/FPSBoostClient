package com.fpsboost.gui.font;

import java.awt.font.*;
import java.util.*;
import java.awt.geom.*;

import com.fpsboost.events.EventManager;
import com.fpsboost.events.misc.TextEvent;
import com.fpsboost.util.RenderUtil;
import net.minecraft.client.renderer.*;
import org.lwjgl.opengl.*;
import java.awt.image.*;
import java.awt.*;
import java.nio.*;
import java.util.List;

public class UnicodeFontRenderer {
    private static final int[] colorCode;
    private final byte[][] charWidth;
    private final int[] textures;
    private final FontRenderContext context;
    private final Font font;
    private float size;
    private int fontWidth;
    private int fontHeight;
    private int textureWidth;
    private int textureHeight;

    public final float drawCenteredString(final String text, final float x, final float y, final int color) {
        return (float)drawString(text, x - getStringWidth(text) / 2F, y, color);
    }

    public final float drawCenteredStringNoFormat(final String text, final float x, final float y, final int color) {
        return (float)drawStringNoFormat(text, x - getStringWidth(text) / 2F, y, color, false);
    }

    public final void drawCenteredStringWithShadow(final String text, final float x, final float y, final int color) {
        drawStringWithShadow(text, x - getStringWidth(text) / 2F, y, color);
    }

    public UnicodeFontRenderer(final Font font) {
        charWidth = new byte[256][];
        textures = new int[256];
        context = new FontRenderContext(new AffineTransform(), true, true);
        size = 0.0f;
        fontWidth = 0;
        fontHeight = 0;
        textureWidth = 0;
        textureHeight = 0;
        this.font = font;
        size = font.getSize2D();
        Arrays.fill(textures, -1);
        final Rectangle2D maxBounds = font.getMaxCharBounds(context);
        fontWidth = (int)Math.ceil(maxBounds.getWidth());
        fontHeight = (int)Math.ceil(maxBounds.getHeight());
        if (fontWidth > 127 || fontHeight > 127) {
            throw new IllegalArgumentException("Font size to large!");
        }
        textureWidth = resizeToOpenGLSupportResolution(fontWidth * 16);
        textureHeight = resizeToOpenGLSupportResolution(fontHeight * 16);
    }

    public String trimStringToWidth(final String p_trimStringToWidth_1_, final int p_trimStringToWidth_2_, final boolean p_trimStringToWidth_3_) {
        final StringBuilder stringbuilder = new StringBuilder();
        int i = 0;
        final int j = p_trimStringToWidth_3_ ? (p_trimStringToWidth_1_.length() - 1) : 0;
        final int k = p_trimStringToWidth_3_ ? -1 : 1;
        boolean flag = false;
        boolean flag2 = false;
        for (int l = j; l >= 0 && l < p_trimStringToWidth_1_.length() && i < p_trimStringToWidth_2_; l += k) {
            final char c0 = p_trimStringToWidth_1_.charAt(l);
            final float i2 = getStringWidth(String.valueOf(c0));
            if (flag) {
                flag = false;
                if (c0 != 'l' && c0 != 'L') {
                    if (c0 == 'r' || c0 == 'R') {
                        flag2 = false;
                    }
                }
                else {
                    flag2 = true;
                }
            }
            else if (i2 < 0) {
                flag = true;
            }
            else {
                i += i2;
                if (flag2) {
                    ++i;
                }
            }
            if (i > p_trimStringToWidth_2_) {
                break;
            }
            if (p_trimStringToWidth_3_) {
                stringbuilder.insert(0, c0);
            }
            else {
                stringbuilder.append(c0);
            }
        }
        return stringbuilder.toString();
    }

    public final int getHeight() {
        return fontHeight / 2;
    }

    public final int getFontHeight() {
        return fontHeight / 2;
    }

    protected final int drawChar(final char chr, final float x, final float y) {
        final int region = chr >> 8;
        final int id = chr & '\u00ff';
        final int xTexCoord = (id & 0xF) * fontWidth;
        final int yTexCoord = (id >> 4) * fontHeight;
        final int width = getOrGenerateCharWidthMap(region)[id];
        GlStateManager.bindTexture(getOrGenerateCharTexture(region));
        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GL11.glBegin(7);
        GL11.glTexCoord2d(wrapTextureCoord(xTexCoord, textureWidth), wrapTextureCoord(yTexCoord, textureHeight));
        GL11.glVertex2f(x, y);
        GL11.glTexCoord2d(wrapTextureCoord(xTexCoord, textureWidth), wrapTextureCoord(yTexCoord + fontHeight, textureHeight));
        GL11.glVertex2f(x, y + fontHeight);
        GL11.glTexCoord2d(wrapTextureCoord(xTexCoord + width, textureWidth), wrapTextureCoord(yTexCoord + fontHeight, textureHeight));
        GL11.glVertex2f(x + width, y + fontHeight);
        GL11.glTexCoord2d(wrapTextureCoord(xTexCoord + width, textureWidth), wrapTextureCoord(yTexCoord, textureHeight));
        GL11.glVertex2f(x + width, y);
        GL11.glEnd();
        return width;
    }

    public int drawString(final String str, final float x, final float y, final int color) {
        return drawString(str, x, y, color, false);
    }

    public final int drawStringNoFormat(String str, float x, float y, int color, final boolean darken) {
        // 内存泄露隐患
        // str = str.replace("\u25ac", "=");
        char targetChar = '\u25ac';
        char replacementChar = '=';

        StringBuilder sb = new StringBuilder();
        for (char c : str.toCharArray()) {
            if (c == targetChar) {
                sb.append(replacementChar);
            } else {
                sb.append(c);
            }
        }
        y -= 2.0f;
        x *= 2.0f;
        y *= 2.0f;
        y -= 2.0f;
        int offset = 0;
        if (darken) {
            color = ((color & 0xFCFCFC) >> 2 | (color & 0xFF000000));
        }
        final float r = (color >> 16 & 0xFF) / 255.0f;
        final float g = (color >> 8 & 0xFF) / 255.0f;
        final float b = (color & 0xFF) / 255.0f;
        float a = (color >> 24 & 0xFF) / 255.0f;
        if (a == 0.0f) {
            a = 1.0f;
        }
        GlStateManager.color(r, g, b, a);
        GL11.glPushMatrix();
        GL11.glScaled(0.5, 0.5, 0.5);
        final char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; ++i) {
            final char chr = chars[i];
            if (chr == '\u00a7' && i != chars.length - 1) {
                ++i;
                color = "0123456789abcdef".indexOf(chars[i]);
                if (color != -1 && darken) {
                    color |= 0x10;
                }
            }
            else {
                offset += drawChar(chr, x + offset, y);
            }
        }
        GL11.glPopMatrix();
        return offset;
    }

    public final int drawString(String str, float x, float y, int color, final boolean darken) {
        // str = str.replace("▬", "=");
        TextEvent textEvent = new TextEvent(str);
        EventManager.call(textEvent);
        if (textEvent.isCancelled()) return 0;
        str  = textEvent.text;
        y -= 2.0f;
        x *= 2.0f;
        y *= 2.0f;
        y -= 2.0f;
        int offset = 0;
        if (darken) {
            color = ((color & 0xFCFCFC) >> 2 | (color & 0xFF000000));
        }
        float r = (color >> 16 & 0xFF) / 255.0f;
        float g = (color >> 8 & 0xFF) / 255.0f;
        float b = (color & 0xFF) / 255.0f;
        float a = (color >> 24 & 0xFF) / 255.0f;
        if (a == 0.0f) {
            a = 1.0f;
        }
        GlStateManager.color(r, g, b, a);
        GL11.glPushMatrix();
        GL11.glScaled(0.5, 0.5, 0.5);
        final char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; ++i) {
            final char chr = chars[i];
            if (chr == '\u00a7' && i != chars.length - 1) {
                ++i;
                color = "0123456789abcdef".indexOf(chars[i]);
                if (color != -1) {
                    if (darken) {
                        color |= 0x10;
                    }
                    color = colorCode[color];
                    r = (color >> 16 & 0xFF) / 255.0f;
                    g = (color >> 8 & 0xFF) / 255.0f;
                    b = (color & 0xFF) / 255.0f;
                    GlStateManager.color(r, g, b, a);
                }
            }
            else {
                offset += drawChar(chr, x + offset, y);
            }
        }
        GL11.glPopMatrix();
        return offset;
    }

    public float getMiddleOfBox(final float height) {
        return height / 2.0f - getHeight() / 2.0f;
    }

    public int getStringWidth(String text) {
        if (text == null) {
            return 0;
        }
        TextEvent textEvent = new TextEvent(text);
        EventManager.call(textEvent);
        if (textEvent.isCancelled()) return 0;
        text  = textEvent.text;
        int width = 0;
        final char[] currentData = text.toCharArray();
        for (int size = text.length(), i = 0; i < size; ++i) {
            final char chr = currentData[i];
            final char character = text.charAt(i);
            if (character == '\u00a7') {
                ++i;
            }
            else {
                width += getOrGenerateCharWidthMap(chr >> 8)[chr & '\u00ff'];
            }
        }
        return width / 2;
    }

    public final float getSize() {
        return size;
    }

    private final int generateCharTexture(final int id) {
        final int textureId = GL11.glGenTextures();
        final int offset = id << 8;
        final BufferedImage img = new BufferedImage(textureWidth, textureHeight, 2);
        final Graphics2D g = (Graphics2D)img.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g.setFont(font);
        g.setColor(Color.WHITE);
        final FontMetrics fontMetrics = g.getFontMetrics();
        for (int y = 0; y < 16; ++y) {
            for (int x = 0; x < 16; ++x) {
                final String chr = String.valueOf((char)(y << 4 | x | offset));
                g.drawString(chr, x * fontWidth, y * fontHeight + fontMetrics.getAscent());
            }
        }
        GL11.glBindTexture(3553, textureId);
        GL11.glTexParameteri(3553, 10241, 9728);
        GL11.glTexParameteri(3553, 10240, 9728);
        GL11.glTexImage2D(3553, 0, 6408, textureWidth, textureHeight, 0, 6408, 5121, imageToBuffer(img));
        return textureId;
    }

    private final int getOrGenerateCharTexture(final int id) {
        if (textures[id] == -1) {
            return textures[id] = generateCharTexture(id);
        }
        return textures[id];
    }

    private int resizeToOpenGLSupportResolution(final int size) {
        int power;
        for (power = 0; size > 1 << power; ++power) {}
        return 1 << power;
    }

    private final byte[] generateCharWidthMap(final int id) {
        final int offset = id << 8;
        final byte[] widthmap = new byte[256];
        for (int i = 0; i < widthmap.length; ++i) {
            widthmap[i] = (byte)Math.ceil(font.getStringBounds(String.valueOf((char)(i | offset)), context).getWidth());
        }
        return widthmap;
    }

    private byte[] getOrGenerateCharWidthMap(final int id) {
        if (charWidth[id] == null) {
            return charWidth[id] = generateCharWidthMap(id);
        }
        return charWidth[id];
    }

    private double wrapTextureCoord(final int coord, final int size) {
        return coord / (double)size;
    }

    private static ByteBuffer imageToBuffer(final BufferedImage img) {
        final int[] arr = img.getRGB(0, 0, img.getWidth(), img.getHeight(), null, 0, img.getWidth());
        final ByteBuffer buf = ByteBuffer.allocateDirect(4 * arr.length);
        for (final int i : arr) {
            buf.putInt(i << 8 | (i >> 24 & 0xFF));
        }
        buf.flip();
        return buf;
    }

    @Override
    protected final void finalize() {
        for (final int textureId : textures) {
            if (textureId != -1) {
                GL11.glDeleteTextures(textureId);
            }
        }
    }

    public final void drawStringWithShadow(final String text, final float x, final float y, final int rgb) {
        drawString(text, x + 0.5f, y + 0.5f, rgb, true);
        drawString(text, x, y, rgb);
    }

    public final void drawLimitedString(final String text, final float x, final float y, final int color, final float maxWidth) {
        drawLimitedStringWithAlpha(text, x, y, color, (color >> 24 & 0xFF) / 255.0f, maxWidth);
    }

    public final void drawLimitedStringWithAlpha(final String text, float x, float y, final int color, final float alpha, final float maxWidth) {
        x *= 2.0f;
        y *= 2.0f;
        final float originalX = x;
        float curWidth = 0.0f;
        GL11.glPushMatrix();
        GL11.glScaled(0.5, 0.5, 0.5);
        final boolean wasBlend = GL11.glGetBoolean(3042);
        GlStateManager.enableAlpha();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3553);
        int currentColor = color;
        final char[] characters = text.toCharArray();
        int index = 0;
        for (final char c : characters) {
            if (c == '\r') {
                x = originalX;
            }
            if (c == '\n') {
                y += getFontHeight() * 2.0f;
            }
            Label_0362: {
                if (c != '\u00a7' && (index == 0 || index == characters.length - 1 || characters[index - 1] != '\u00a7')) {
                    if (index >= 1 && characters[index - 1] == '§') {
                        break Label_0362;
                    }
                    GL11.glPushMatrix();
                    drawString(Character.toString(c), x, y, RenderUtil.reAlpha(new Color(currentColor), (int)alpha).getRGB(), false);
                    GL11.glPopMatrix();
                    curWidth += getStringWidth(Character.toString(c)) * 2.0f;
                    x += getStringWidth(Character.toString(c)) * 2.0f;
                    if (curWidth > maxWidth) {
                        break;
                    }
                }
                else if (c == ' ') {
                    x += getStringWidth(" ");
                }
                else if (c == '§' && index != characters.length - 1) {
                    final int codeIndex = "0123456789abcdefklmnor".indexOf(text.charAt(index + 1));
                    if (codeIndex < 0) {
                        break Label_0362;
                    }
                    if (codeIndex < 16) {
                        currentColor = colorCode[codeIndex];
                    }
                    else if (codeIndex == 21) {
                        currentColor = Color.WHITE.getRGB();
                    }
                }
                ++index;
            }
        }
        if (!wasBlend) {
            GL11.glDisable(3042);
        }
        GL11.glPopMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public final void drawOutlinedString(final String str, final float x, final float y, final int internalCol, final int externalCol) {
        drawString(str, x - 0.5f, y, externalCol);
        drawString(str, x + 0.5f, y, externalCol);
        drawString(str, x, y - 0.5f, externalCol);
        drawString(str, x, y + 0.5f, externalCol);
        drawString(str, x, y, internalCol);
    }

    public void drawStringWithShadow(final String z, final double x, final double positionY, final int mainTextColor) {
        drawStringWithShadow(z, (float)x, (float)positionY, mainTextColor);
    }

    public double getStringHeight() {
        return getHeight();
    }

    public float drawStringWithShadow(final String text, final double x, final double y, final double sWidth, final int color) {
        final float shadowWidth = (float)drawString(text, (float)(x + sWidth), (float)(y + sWidth), color, true);
        return Math.max(shadowWidth, (float)drawString(text, (float)x, (float)y, color, false));
    }

    static {
        colorCode = new int[32];
        for (int i = 0; i < 32; ++i) {
            final int base = (i >> 3 & 0x1) * 85;
            int r = (i >> 2 & 0x1) * 170 + base;
            int g = (i >> 1 & 0x1) * 170 + base;
            int b = (i & 0x1) * 170 + base;
            if (i == 6) {
                r += 85;
            }
            if (i >= 16) {
                r /= 4;
                g /= 4;
                b /= 4;
            }
            colorCode[i] = ((r & 0xFF) << 16 | (g & 0xFF) << 8 | (b & 0xFF));
        }
    }

    private final List<String> lines = new ArrayList<>();

    private void wrapTextToLines(String text, float x, float width) {
        lines.clear();
        String[] words = text.trim().split(" ");
        StringBuilder line = new StringBuilder();
        for (String word : words) {
            float totalWidth = getStringWidth(line + " " + word);

            if (x + totalWidth >= x + width) {
                lines.add(line.toString());
                line = new StringBuilder(word).append(" ");
                continue;
            }

            line.append(word).append(" ");
        }
        lines.add(line.toString());
    }

    //Lines list and height
    public List<String> getWrappedLines(String text, float x, float width, float heightIncrement) {
        wrapTextToLines(text, x, width);
        return lines;
    }

    public float drawWrappedText(String text, float x, float y, int color, float width, float heightIncrement) {
        wrapTextToLines(text, x, width);

        float newY = y;
        for (String s : lines) {
            RenderUtil.resetColor();
            drawString(s, x, newY, color);
            newY += getHeight() + heightIncrement;
        }
        return newY - y;
    }
}