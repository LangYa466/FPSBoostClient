package com.fpsboost.module.render;

import com.fpsboost.Access;
import com.fpsboost.annotations.system.Binding;
import com.fpsboost.annotations.system.Enable;
import com.fpsboost.annotations.system.Module;
import com.fpsboost.gui.clickGui.book.ModernClickGui;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import com.fpsboost.gui.clickGui.drop.ClickGuiScreen;
import com.fpsboost.module.Category;

@Module(name = "ClickGui",description = "可视化管理模块页面 就是你现在看见的这个页面",category = Category.GUI)
@Binding(Keyboard.KEY_RSHIFT)
public class ClickGui implements Access.InstanceAccess {

    public static GuiScreen modernClickGui = new ModernClickGui();
    @Enable
    public void onEnable(ClickGuiScreen clickGuiScreen){
        setEnable(this,false);
        mc.displayGuiScreen(modernClickGui);
    }
}
