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
import com.fpsboost.util.animations.Animation;
import com.fpsboost.util.animations.Direction;
import com.fpsboost.util.animations.impl.SmoothStepAnimation;
import com.fpsboost.util.render.ColorUtil;
import com.fpsboost.util.render.RoundedUtil;
import com.fpsboost.value.impl.ColorValue;
import com.fpsboost.value.impl.NumberValue;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

import java.awt.*;

/**
 * @author LangYa466,Bzdhyp
 * @since 2024/4/25 20:32
 */

@Module(name = "KeyStore",description = "按键显示",category = Category.GUI)
public class KeyStore  implements Access.InstanceAccess {
    public static ColorValue colorValue = new ColorValue("背景颜色",new Color(0,0,0));
    private static final NumberValue opacity = new NumberValue("不透明度", 0.25, 0.0, 1, .05);
    private static final NumberValue radius = new NumberValue("圆角", 3, 1, 17.5, .5);
    private final NumberValue offsetValue = new NumberValue("间隔", 3, 2.5, 10, .5);
    private final NumberValue sizeValue = new NumberValue("大小", 25, 15, 35, 1);

    private final Dragging dragging = Access.getInstance().getDragManager().createDrag(this.getClass(), "Keystrokes", 70, 70);

    private Button keyBindForward;
    private Button keyBindLeft;
    private Button keyBindBack;
    private Button keyBindRight;
    private Button keyBindJump;

    @EventTarget
    public void onRender(Render2DEvent e) {
        float offset = offsetValue.getValue().floatValue();
        dragging.setHeight((float) ((sizeValue.getValue() + offset) * 3) - offset);
        dragging.setWidth((float) ((sizeValue.getValue() + offset) * 3) - offset);

        if (keyBindForward == null) {
            keyBindForward = new Button(mc.gameSettings.keyBindForward);
            keyBindLeft = new Button(mc.gameSettings.keyBindLeft);
            keyBindBack = new Button(mc.gameSettings.keyBindBack);
            keyBindRight = new Button(mc.gameSettings.keyBindRight);
            keyBindJump = new Button(mc.gameSettings.keyBindJump);
        }

        float x = dragging.getX(), y = dragging.getY(), width = dragging.getWidth(), size = sizeValue.getValue().floatValue();

        float increment = size + offset;
        keyBindForward.render(x + width / 2f - size / 2f, y, size);
        keyBindLeft.render(x, y + increment, size);
        keyBindBack.render(x + increment, y + increment, size);
        keyBindRight.render(x + (increment * 2), y + increment, size);
        keyBindJump.render(x, y + increment * 2, width, size);
    }


    public static class Button {
        private static final UnicodeFontRenderer font = FontManager.S22;
        private final KeyBinding binding;
        private final Animation clickAnimation = new SmoothStepAnimation(125, 1);

        public Button(KeyBinding binding) {
            this.binding = binding;
        }

        public void render(float x, float y, float size) {
            render(x, y, size, size);
        }

        public void render(float x, float y, float width, float height) {
            Color color = ColorUtil.applyOpacity(KeyStore.colorValue.getValue(), opacity.getValue().floatValue());
            clickAnimation.setDirection(binding.isKeyDown() ? Direction.FORWARDS : Direction.BACKWARDS);

            RoundedUtil.drawRound(x, y, width, height, radius.getValue().floatValue(), color);
            float offsetX = 0;
            int offsetY = 0;

            font.drawCenteredString(Keyboard.getKeyName(binding.getKeyCode()), x + width / 2 + offsetX - 0.1F, y + height / 2 - font.getHeight() / 2f + offsetY + 2.3F, Color.WHITE.getRGB());

            if (!clickAnimation.finished(Direction.BACKWARDS)) {
                float animation = clickAnimation.getOutput().floatValue();
                Color color2 = ColorUtil.applyOpacity(Color.WHITE, (0.5f * animation));
                RenderUtil.scaleStart(x + width / 2f, y + height / 2f, animation);
                float diff = (height / 2f) - radius.getValue().floatValue();
                RoundedUtil.drawRound(x, y, width, height, ((height / 2f) - (diff * animation)), color2);
                RenderUtil.scaleEnd();
            }
        }
    }
}