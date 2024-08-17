package com.fpsboost.gui.ui;

import com.fpsboost.gui.font.FontManager;
import com.fpsboost.module.handlers.ModuleHandle;
import com.fpsboost.util.HoveringUtil;
import com.fpsboost.util.MathUtils;
import com.fpsboost.util.RenderUtil;
import com.fpsboost.util.StringUtils;
import com.fpsboost.util.animations.Animation;
import com.fpsboost.util.animations.Direction;
import com.fpsboost.util.animations.impl.DecelerateAnimation;
import com.fpsboost.util.render.ColorUtil;
import com.fpsboost.util.render.RoundedUtil;
import com.fpsboost.value.AbstractValue;
import com.fpsboost.value.impl.BooleanValue;
import com.fpsboost.value.impl.ComboValue;
import com.fpsboost.value.impl.NumberValue;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;


public class SettingsPanel extends Panel {

    private final ModuleHandle module;
    private final HashMap<AbstractValue<?>, Animation> booleanSettingHashMap;

    private final HashMap<AbstractValue<?>, Animation> modeSettingHashMap;
    private final HashMap<AbstractValue<?>, Boolean> modeSettingClick;
    private final HashMap<AbstractValue<?>, HashMap<String, Animation>> modesHoverAnimation;
    private final HashMap<AbstractValue<?>, Animation> multiBoolSettingMap;
    private final HashMap<AbstractValue<?>, Boolean> multiBoolSettingClickMap;
    private final HashMap<NumberValue, Float> sliderMap;
    private final HashMap<AbstractValue<?>, HashMap<BooleanValue, Animation>> boolHoverAnimation;
  //  private final HashMap<StringSetting, TextField> textFieldMap;
    public AbstractValue<?> draggingNumber;
    public float maxScroll = 0;
    private boolean hueFlag;
    private boolean saturationFlag;
    public boolean typing;

    public SettingsPanel(ModuleHandle module) {
        this.module = module;
        booleanSettingHashMap = new HashMap<>();

        multiBoolSettingMap = new HashMap<>();
        multiBoolSettingClickMap = new HashMap<>();
        boolHoverAnimation = new HashMap<>();
        sliderMap = new HashMap<>();

        modeSettingHashMap = new HashMap<>();
        modeSettingClick = new HashMap<>();
        modesHoverAnimation = new HashMap<>();
        // textFieldMap = new HashMap<>();

        for (AbstractValue<?> setting : module.getValues()) {
            if (setting instanceof BooleanValue) {
                booleanSettingHashMap.put(setting, new DecelerateAnimation(250, 1));
            }

            if (setting instanceof NumberValue) {
                sliderMap.put((NumberValue) setting, 0f);
            }

            if (setting instanceof ComboValue) {
                HashMap<String, Animation> modesHashmap = new HashMap<>();
                modeSettingHashMap.put(setting, new DecelerateAnimation(250, 1));
                modeSettingClick.put(setting, false);
                ComboValue modeSetting = (ComboValue) setting;

                for (String mode : modeSetting.getStrings()) {
                    modesHashmap.put(mode, new DecelerateAnimation(250, 5));

                }

                modesHoverAnimation.put(setting, modesHashmap);
            }

            /*
            if (setting instanceof StringSetting) {
                StringSetting stringSetting = (StringSetting) setting;
                TextField textField = new TextField(tenacityFont18);
                textField.setText(stringSetting.getString());
                textField.setCursorPositionZero();
                textFieldMap.put(stringSetting, textField);
            }

             */

        }


    }


    @Override
    public void initGui() {
        /*
        if (textFieldMap != null) {
            for (Map.Entry<StringSetting, TextField> entry : textFieldMap.entrySet()) {
                entry.getValue().setText(entry.getKey().getString());
                entry.getValue().setCursorPositionZero();
            }
        }

         */
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        /*
        if (textFieldMap != null) {
            for (Map.Entry<StringSetting, TextField> entry : textFieldMap.entrySet()) {
                entry.getValue().keyTyped(typedChar, keyCode);
            }
        }


         */
    }



    @Override
    public void drawScreen(int mouseX, int mouseY) {
        handle(mouseX, mouseY, -1, GuiEvents.DRAW);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        handle(mouseX, mouseY, button, GuiEvents.CLICK);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        handle(mouseX, mouseY, state, GuiEvents.RELEASE);
    }


    public void handle(int mouseX, int mouseY, int button, GuiEvents type) {
        typing = false;
        if (type == GuiEvents.RELEASE && button == 0) {
            draggingNumber = null;
        }

        Pair<Color, Color> colors = Pair.of(new Color(0x4A4DAC), new Color(0xFFFFFF));


        float count = 0;
        float settingHeight = 44;
        for (AbstractValue<?> setting : module.getValues()) {
            if (setting.isHidden()) continue;
            float settingY = y + 100 + count * 22;
            boolean isHoveringSetting = HoveringUtil.isHovering(x + 5, settingY, 130, 20, mouseX, mouseY);
            if (setting instanceof BooleanValue) {
                float settingY1 = y + 40 + count * 22;
                boolean isHoveringSetting1 = HoveringUtil.isHovering(x + 5, settingY1, 130, 15, mouseX, mouseY);
                BooleanValue booleanSetting = (BooleanValue) setting;
                Animation animation = booleanSettingHashMap.get(setting);
                animation.setDirection(booleanSetting.getValue() ? Direction.FORWARDS : Direction.BACKWARDS);

                if (type == GuiEvents.CLICK && isHoveringSetting1 && button == 0) {
                    booleanSetting.toggle();
                }

                int color = ColorUtil.interpolateColor(new Color(30, 31, 35), colors.getFirst(), (float) animation.getOutput().floatValue());
                FontManager.M16.drawString(setting.getName(), x + 13, settingY1 + FontManager.M16.getMiddleOfBox(8), -1);
                //  RenderUtil.renderRoundedRect(x + 95, settingY, 20, 10, 4,
                //color);

                GL11.glEnable(GL11.GL_BLEND);
                RoundedUtil.drawRound(x + 109, settingY1, 18, 8, 4, ColorUtil.interpolateColorC(new Color(30, 31, 35), colors.getFirst(), (float) animation.getOutput().floatValue()));

                GlStateManager.color(1, 1, 1);
                RenderUtil.drawGoodCircle(x + 113 + (10 * animation.getOutput().floatValue()), settingY1 + 4, 4f, -1);
                count -= .6f;
            }

            if (setting instanceof ComboValue) {
                ComboValue modeSetting = (ComboValue) setting;
                Animation animation = modeSettingHashMap.get(setting);
                float settingY1 = y + 70 + count * 22;
                boolean isHoveringSetting1 = HoveringUtil.isHovering(x + 5, settingY1, 130, 15, mouseX, mouseY);

                if (type == GuiEvents.CLICK && isHoveringSetting1 && button == 1) {
                    modeSettingClick.put(setting, !modeSettingClick.get(setting));
                }


                float stringWidth = (float) FontManager.M16.getStringWidth(StringUtils.getLongestModeName(modeSetting.getAsArray()));

                animation.setDirection(modeSettingClick.get(setting) ? Direction.FORWARDS : Direction.BACKWARDS);

                float modeWidth = 15 + stringWidth;

                float math = (modeSetting.getAsArray().size() - 1) * 15;


                float boxX = (x + 114 - stringWidth) - 1;
                float nameWidth = FontManager.M16.getStringWidth(modeSetting.getName());
                float stringX = x + 13;
                if (nameWidth > (boxX - stringX)) {
                    FontManager.M16.drawWrappedText(modeSetting.getName(), stringX, settingY1 - (FontManager.M16.getMiddleOfBox(12) / 2) - .5f, -1, boxX - stringX, 2);
                    //    FontManager.M16.drawString(modeSetting.name, stringX + (boxX - stringX) / 2f, settingY + FontManager.M16.getMiddleOfBox(8), -1);
                } else {
                    FontManager.M16.drawString(modeSetting.getName(), x + 13, settingY1 + 1.5f, -1);
                }


                RoundedUtil.drawRound(boxX, settingY1 - 1, modeWidth + 2, 12 + (float) (math * animation.getOutput().floatValue()), 4, new Color(68, 71, 78));
                //  RenderUtil.renderRoundedRect((float) (x + 113.5 - stringWidth), settingY - 1.5f, modeWidth + 3, 14 + (float) (math * animation.getOutput().floatValue()),
                //      4, new Color(68, 71, 78).getRGB());

                RoundedUtil.drawRound(x + 114 - stringWidth, settingY1, modeWidth, 10 + (float) (math * animation.getOutput().floatValue()), 3, new Color(30, 31, 35));


                float modeX = x + 114 - stringWidth + modeWidth / 2f;
                RenderUtil.resetColor();
                FontManager.M16.drawCenteredString(modeSetting.getValue(), modeX, settingY1 + FontManager.M16.getMiddleOfBox(10) + 1.3f, -1);

                int modeCount = 1;
                for (String mode : modeSetting.getStrings()) {
                    if (!mode.equalsIgnoreCase(Arrays.toString(modeSetting.getStrings()))) {
                        Animation modeAnimation = modesHoverAnimation.get(modeSetting).get(mode);
                        boolean isHoveringMode = animation.getDirection().equals(Direction.FORWARDS) &&
                                HoveringUtil.isHovering(x + 115 - stringWidth, settingY1 + (modeCount * 15), modeWidth, 11, mouseX, mouseY);


                        if (type == GuiEvents.CLICK && button == 0 && isHoveringMode) {
                            modeSettingClick.put(setting, !modeSettingClick.get(setting));
                            modeSetting.setValue(mode);
                        }

                        modeAnimation.setDirection(isHoveringMode ? Direction.FORWARDS : Direction.BACKWARDS);
                        modeAnimation.setDuration(isHoveringMode ? 200 : 570);

                        int colorInterpolate = ColorUtil.interpolateColor(new Color(128, 134, 141), colors.getSecond(), (float) modeAnimation.getOutput().floatValue());

                        GlStateManager.color(1, 1, 1, 1);
                        FontManager.M16.drawCenteredString(mode, modeX, (float) (settingY1 + 2 + ((modeCount * 15) * animation.getOutput().floatValue())),
                                ColorUtil.interpolateColor(new Color(40, 40, 40, 10), new Color(colorInterpolate), (float) animation.getOutput().floatValue()));
                        modeCount++;
                    }
                }

                count += ((math / settingHeight) * animation.getOutput().floatValue()) + 1f;
            }


            /*
            if (setting instanceof ColorSetting) {
                ColorSetting colorSetting = (ColorSetting) setting;
                float height = (colorSetting.isRainbow() ? 100 : 90);
                RoundedUtil.drawRound(x + 12, settingY - 1, 117, height, 4, new Color(68, 71, 78));
                RoundedUtil.drawRound(x + 13, settingY, 115, height - 2, 3, new Color(30, 31, 35));
                //   RenderUtil.renderRoundedRect(x + 8.5f, settingY - 4.5f, 118, 85, 4, new Color(68, 71, 78).getRGB());
                //  RenderUtil.renderRoundedRect(x + 10, settingY - 3, 115, 82, 3, new Color(30, 31, 35).getRGB());
                FontManager.M16.drawCenteredString(colorSetting.name, (x + 13) + 115 / 2f, settingY + 3, -1);

                if (colorSetting.isRainbow()) {
                    Color color = colorSetting.getColor();
                    int red = color.getRed(), green = color.getGreen(), blue = color.getBlue();
                    float[] hsb = Color.RGBtoHSB(red, green, blue, null);
                    colorSetting.setHue(hsb[0]);
                    colorSetting.setSaturation(hsb[1]);
                    colorSetting.setBrightness(hsb[2]);
                }


                float[] hsb = {(float) colorSetting.getHue(), (float) colorSetting.getSaturation(), (float) colorSetting.getBrightness()};

                /*Draw.colorRGBA(colorSetting.getColor().getRGB());
                GL11.glEnable(GL11.GL_BLEND);
                mc.getTextureManager().bindTexture(new ResourceLocation("Tenacity/booleanslider1.png"));
                Gui.drawModalRectWithCustomSizedTexture(x + 98, settingY, 0, 0, 20, 10, 20, 10);*/
                //RenderUtil.renderRoundedRect(x + 95, settingY, 20, 10, 3, colorSetting.getColor().getRGB());

            /*
                float gradientX = x + 17;
                float gradientY = settingY + 15;
                float gradientWidth = 97;
                float gradientHeight = 50;


                if (button == 0 && type == GuiEvents.CLICK) {
                    if (HoveringUtil.isHovering(gradientX, gradientY + gradientHeight + 3, (gradientWidth + 10), 6, mouseX, mouseY)) {
                        draggingNumber = setting;
                        hueFlag = true;
                    }
                    if (HoveringUtil.isHovering(gradientX, gradientY, gradientWidth, gradientHeight, mouseX, mouseY)) {
                        draggingNumber = setting;
                        hueFlag = false;
                    }
                }

                if (draggingNumber != null && draggingNumber.equals(setting)) {
                    if (hueFlag) {
                        colorSetting.setHue(Math.min(1, Math.max(0, (mouseX - gradientX) / (gradientWidth + 10))));
                    } else {
                        colorSetting.setBrightness(Math.min(1, Math.max(0, 1 - ((mouseY - gradientY) / gradientHeight))));
                        colorSetting.setSaturation(Math.min(1, Math.max(0, (mouseX - gradientX) / gradientWidth)));
                    }
                }


                //Picker gradients
                Gui.drawRect2(gradientX, gradientY, gradientWidth, gradientHeight, Color.getHSBColor(hsb[0], 1, 1).getRGB());
                Gui.drawGradientRectSideways2(gradientX, gradientY, gradientWidth, gradientHeight, Color.getHSBColor(hsb[0], 0, 1).getRGB(),
                        ColorUtil.applyOpacity(Color.getHSBColor(hsb[0], 0, 1).getRGB(), 0));

                Gui.drawGradientRect2(gradientX, gradientY, gradientWidth, gradientHeight,
                        ColorUtil.applyOpacity(Color.getHSBColor(hsb[0], 1, 0).getRGB(), 0), Color.getHSBColor(hsb[0], 1, 0).getRGB());

                float pickerY = gradientY + (gradientHeight * (1 - hsb[2]));
                float pickerX = gradientX + (gradientWidth * hsb[1] - 1);
                pickerY = Math.max(Math.min(gradientY + gradientHeight - 2, pickerY), gradientY);
                pickerX = Math.max(Math.min(gradientX + gradientWidth - 2, pickerX), gradientX);

                GL11.glEnable(GL11.GL_BLEND);
                RenderUtil.color(-1);
                mc.getTextureManager().bindTexture(new ResourceLocation("Tenacity/colorpicker2.png"));
                Gui.drawModalRectWithCustomSizedTexture(pickerX, pickerY, 0, 0, 4, 4, 4, 4);


                GlStateManager.color(1, 1, 1, 1);
                Gui.drawRect2(gradientX + gradientWidth + 5, gradientY, 5, gradientHeight, colorSetting.getColor().getRGB());


                // Hue bar
                RenderUtil.color(-1);
                mc.getTextureManager().bindTexture(new ResourceLocation("Tenacity/hue.png"));
                Gui.drawModalRectWithCustomSizedTexture(gradientX, gradientY + gradientHeight + 4.5f, 0, 0, gradientWidth + 10, 4, gradientWidth + 10, 4);
                GlStateManager.color(1, 1, 1, 1);

                //Hue slider
                Gui.drawRect2(gradientX + ((gradientWidth + 10) * hsb[0]), gradientY + gradientHeight + 3.5, 1, 6, -1);


                FontManager.M14.drawString("Rainbow: ", gradientX, gradientY + gradientHeight + 14, -1);

                String text = colorSetting.isRainbow() ? "Enabled" : "Disabled";
                float textX = gradientX + FontManager.M14.getStringWidth("Rainbow: ") + 4;
                float textY = gradientY + gradientHeight + 13;

                boolean hoveringRound = HoveringUtil.isHovering(textX, textY, FontManager.M14.getStringWidth(text) + 4, FontManager.M14.getHeight() + 2, mouseX, mouseY);

                if (type == GuiEvents.CLICK && hoveringRound && button == 0) {
                    colorSetting.setRainbow(!colorSetting.isRainbow());
                }
                Color roundColor = colorSetting.isRainbow() ? new Color(0, 203, 33) : new Color(203, 0, 33);
                RoundedUtil.drawRound(textX, textY, FontManager.M14.getStringWidth(text) + 4, FontManager.M14.getHeight() + 2, 3,
                        hoveringRound ? roundColor.brighter() : roundColor);

                FontManager.M14.drawCenteredString(text, textX + (FontManager.M14.getStringWidth(text) + 4) / 2f,
                        textY + FontManager.M14.getMiddleOfBox(FontManager.M14.getHeight() + 2), -1);


                if (colorSetting.isRainbow()) {

                    Gui.drawGradientRectSideways2(gradientX, textY + 12, gradientWidth + 10, 4, Color.WHITE.getRGB(), Color.RED.getRGB());

                    Gui.drawRect2(gradientX + ((gradientWidth + 10) * hsb[1]), textY + 11, 1, 6, Color.BLACK.getRGB());

                    boolean hoveringSat = HoveringUtil.isHovering(gradientX, textY + 10, gradientWidth + 10, 6, mouseX, mouseY);

                    if (type == GuiEvents.CLICK && hoveringSat && button == 0) {
                        saturationFlag = true;
                        draggingNumber = setting;
                    }

                    if (type == GuiEvents.RELEASE && saturationFlag) {
                        saturationFlag = false;
                    }

                    if (saturationFlag) {
                        colorSetting.getRainbow().setSaturation(Math.min(1, Math.max(0, (mouseX - gradientX) / (gradientWidth + 10))));
                    }

                }


                count += 3.5 + (colorSetting.isRainbow() ? .5f : 0);
            }
        */
            if (setting instanceof NumberValue) {
                isHoveringSetting = HoveringUtil.isHovering(x + 5, settingY, 130, 20, mouseX, mouseY);
                NumberValue numberSetting = (NumberValue) setting;
                FontManager.M16.drawString(setting.getName(), x + 13, settingY + 2, -1);

                if (type == GuiEvents.CLICK && isHoveringSetting && button == 0) {
                    draggingNumber = numberSetting;
                }

                double currentValue = numberSetting.getValue();


                if (draggingNumber != null && draggingNumber == setting) {
                    float percent = Math.min(1, Math.max(0, (mouseX - (x + 14)) / 108));
                    double newValue = (percent * (numberSetting.getMaximum() - numberSetting.getMinimum())) + numberSetting.getMinimum();
                    numberSetting.setValue(newValue);
                }

                String value = Double.toString(MathUtils.round(currentValue, 2));
                FontManager.M16.drawString(value, x + 120 - FontManager.M16.getStringWidth(value), settingY + 2, -1);

                float sliderMath = (float) (((currentValue) - numberSetting.getMinimum())
                        / (numberSetting.getMaximum() - numberSetting.getMinimum()));


                RoundedUtil.drawRound(x + 12, settingY + 13, 110, 5, 2, new Color(30, 31, 35));


                Color sliderColor = colors.getFirst();
                float[] hsb = Color.RGBtoHSB(sliderColor.getRed(), sliderColor.getGreen(), sliderColor.getBlue(), null);
                float saturation = hsb[1] * sliderMath;

                float totalSliderWidth = 108;

                sliderMap.put(numberSetting, (float) RenderUtil.animate(totalSliderWidth * sliderMath, sliderMap.get(numberSetting), .1));


                RoundedUtil.drawRound(x + 13, settingY + 14, Math.max(4, sliderMap.get(numberSetting)), 3, 1.5f, new Color(Color.HSBtoRGB(hsb[0], saturation, hsb[2])));

                count += 1F;
            }
            /*
            if (setting instanceof StringSetting) {
                StringSetting stringSetting = (StringSetting) setting;

                // RenderUtil.renderRoundedRect(x + 13, settingY - 3, 115, 40, 3, new Color(30, 31, 35).getRGB());
                FontManager.M16.drawString(setting.name, x + 16, settingY + 1, -1);

                TextField textField = textFieldMap.get(stringSetting);

                textField.setBackgroundText("Type here...");
                textField.setXPosition(x + 17);
                textField.setYPosition(settingY + 12);
                textField.setWidth(100);
                textField.setHeight(15);
                textField.setOutline(new Color(68, 71, 78));
                textField.setFill(new Color(30, 31, 35));

                stringSetting.setString(textField.getText());

                if (type == GuiEvents.CLICK) textField.mouseClicked(mouseX, mouseY, button);

                if (!typing) {
                    typing = textField.isFocused();
                }


                textField.drawTextBox();

                count += .5f;
            }

             */
        }
        maxScroll = count * settingHeight;
    }


}
