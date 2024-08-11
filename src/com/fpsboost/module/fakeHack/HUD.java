package com.fpsboost.module.fakeHack;

import com.fpsboost.Access;
import com.fpsboost.annotations.event.EventTarget;
import com.fpsboost.annotations.system.Module;
import com.fpsboost.annotations.system.Startup;
import com.fpsboost.gui.drag.Dragging;
import com.fpsboost.gui.font.FontManager;
import com.fpsboost.gui.font.UnicodeFontRenderer;
import com.fpsboost.module.ModuleManager;
import com.fpsboost.module.boost.Sprint;
import com.fpsboost.util.LiteInvoke;
import com.fpsboost.events.render.Render2DEvent;
import com.fpsboost.module.Category;

@Module(value = "客户端Logo",category = Category.ClientSetting)
@Startup // enable when client startup
public class HUD implements Access.InstanceAccess {

    @LiteInvoke.Autowired
    private Sprint sprint;

    @LiteInvoke.Autowired
    private ModuleManager moduleManager;

    private final Dragging pos = Access.getInstance().getDragManager().createDrag(this.getClass(),"logo", 4, 4);

    /**
     * Subscribe a {@link Render2DEvent}
     *
     * @param event Event
     */
    @EventTarget
    public void onRender2D(Render2DEvent event) {
        UnicodeFontRenderer fontRenderer = FontManager.M18;
        String name = "FPSBoost";
        pos.setWidth(fontRenderer.getStringWidth(name));
        pos.setHeight(fontRenderer.getHeight());
        FontManager.M18.drawStringWithShadow(name, pos.getX(), pos.getY(), -1);
    }

}
