package com.fpsboost.module.boost;

import com.fpsboost.Access;
import com.fpsboost.annotations.event.EventTarget;
import com.fpsboost.annotations.system.Module;
import com.fpsboost.events.misc.ClickEvent;
import com.fpsboost.module.Category;
import com.fpsboost.util.SoundUtil;
import com.fpsboost.value.impl.ComboValue;
import com.fpsboost.value.impl.NumberValue;
import org.apache.commons.lang3.RandomUtils;

@Module(name = "ClickSounds",cnName = "模拟点击音效", description = "让你每次按下鼠标都能模拟特殊鼠标声音", category = Category.Boost)
public final class ClickSounds implements Access.InstanceAccess {

    private final ComboValue sound = new ComboValue("模式", "Standard","Standard","Double","Alan");

    private final NumberValue volume = new NumberValue("音量", 0.5, 0.1, 2, 0.1);
    private final NumberValue variation = new NumberValue("时长", 5, 0, 100, 1);

    @EventTarget
    public void onClick(ClickEvent event) {
        String soundName = "rise.click.standard";

        switch (sound.getValue()) {
            case "Double": {
                soundName = "rise.click.double";
                break;
            }

            case "Alan": {
                soundName = "rise.click.alan";
                break;
            }
        }

        SoundUtil.playSound(soundName, volume.getValue().floatValue(), RandomUtils.nextFloat(1.0F, 1 + variation.getValue().floatValue() / 100f));
    }
}