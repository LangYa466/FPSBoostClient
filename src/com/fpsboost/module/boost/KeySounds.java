package com.fpsboost.module.boost;

import com.fpsboost.Access;
import com.fpsboost.annotations.event.EventTarget;
import com.fpsboost.annotations.system.Module;
import com.fpsboost.events.misc.KeyInputEvent;
import com.fpsboost.module.Category;
import com.fpsboost.util.SoundUtil;
import com.fpsboost.value.impl.NumberValue;
import org.apache.commons.lang3.RandomUtils;
import org.lwjgl.input.Keyboard;

@Module(name = "KeySounds",cnName = "模拟键盘音效", description = "让你每次按下键盘的任意一个键都能模拟特殊键盘声音", category = Category.Boost)
public final class KeySounds implements Access.InstanceAccess {

    private final NumberValue volume = new NumberValue("音量", 0.5, 0.1, 2, 0.1);

    @EventTarget
    public void onKeyInput(KeyInputEvent event) {
        String soundName;
        if (event.getKey() == Keyboard.KEY_SPACE) soundName = "langya.key.space"; else soundName = "langya.key.key";
        SoundUtil.playSound(soundName, volume.getValue().floatValue(), RandomUtils.nextFloat(1.0F, 1));
    }
}