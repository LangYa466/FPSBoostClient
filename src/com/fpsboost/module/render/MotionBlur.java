package com.fpsboost.module.render;

import com.fpsboost.Access;
import com.fpsboost.annotations.event.EventTarget;
import com.fpsboost.annotations.system.Disable;
import com.fpsboost.annotations.system.Enable;
import com.fpsboost.annotations.system.Module;
import com.fpsboost.events.update.TickEvent;
import com.fpsboost.module.Category;
import com.fpsboost.util.motionBlur.MotionBlurResourceManager;
import com.fpsboost.value.impl.NumberValue;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;

import java.lang.reflect.Field;
import java.util.Map;

@Module(name = "MotionBlur",cnName = "动态模糊",description = "动态模糊 但是不能在快速渲染使用",category = Category.GUI)
public class MotionBlur implements Access.InstanceAccess {
    public static final NumberValue amount = new NumberValue("模糊度", 2, 1, 10,1);

    float lastValue = 0F;
    private Map<String, MotionBlurResourceManager> domainResourceManagers;

    @Disable
    public void onDisable() {
        mc.entityRenderer.stopUseShader();
    }

    @Enable
    public void onEnable() {
        if(this.domainResourceManagers == null) {
            try {
                Field[] fields = SimpleReloadableResourceManager.class.getDeclaredFields();
                for (Field field : fields) {
                    if (field.getType() == Map.class) {
                        field.setAccessible(true);
                        this.domainResourceManagers = (Map<String, MotionBlurResourceManager>) field.get(mc.getResourceManager());
                        break;
                    }
                }
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }

        if(!this.domainResourceManagers.containsKey("motionblur")) {
            this.domainResourceManagers.put("motionblur", new MotionBlurResourceManager());
        }

        this.lastValue = amount.getValue().floatValue();
        applyShader();
    }

    public boolean isFastRenderEnabled() {
        try {
            Field fastRender = GameSettings.class.getDeclaredField("ofFastRender");
            return fastRender.getBoolean(mc.gameSettings);
        } catch (Exception exception) {
            return false;
        }
    }

    public void applyShader() {
        mc.entityRenderer.loadShader(new ResourceLocation("motionblur", "motionblur"));
    }

    @EventTarget
    public void onTick(TickEvent event) {
        if((!mc.entityRenderer.isShaderActive() || this.lastValue != amount.getValue()) && mc.theWorld != null && !isFastRenderEnabled()) {
            this.lastValue = amount.getValue().floatValue();
            applyShader();
        }
        if(this.domainResourceManagers == null) {
            try {
                Field[] fields = SimpleReloadableResourceManager.class.getDeclaredFields();
                for (Field field : fields) {
                    if (field.getType() == Map.class) {
                        field.setAccessible(true);
                        this.domainResourceManagers = (Map<String, MotionBlurResourceManager>) field.get(mc.getResourceManager());
                        break;
                    }
                }
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }
        if(!this.domainResourceManagers.containsKey("motionblur")) {
            this.domainResourceManagers.put("motionblur", new MotionBlurResourceManager());
        }
    }

}
