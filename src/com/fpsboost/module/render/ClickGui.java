package com.fpsboost.module.render;

import com.fpsboost.Access;
import com.fpsboost.annotations.system.Binding;
import com.fpsboost.annotations.system.Enable;
import com.fpsboost.annotations.system.Module;
import org.lwjgl.input.Keyboard;
import com.fpsboost.gui.click.ClickGuiScreen;
import com.fpsboost.module.Category;

@Module(value = "ClickGui",category = Category.GUI)
@Binding(Keyboard.KEY_RSHIFT)
public class ClickGui implements Access.InstanceAccess {
    @Enable
    public void onEnable(ClickGuiScreen clickGuiScreen){
        setEnable(this,false);
        mc.displayGuiScreen(clickGuiScreen);
    }
}
