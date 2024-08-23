package com.fpsboost.module.boost;

import com.fpsboost.Access;
import com.fpsboost.annotations.event.EventTarget;
import com.fpsboost.annotations.system.Module;
import com.fpsboost.events.network.PacketReceiveEvent;
import com.fpsboost.events.network.PacketSendEvent;
import com.fpsboost.events.update.UpdateEvent;
import com.fpsboost.module.Category;
import com.fpsboost.value.impl.NumberValue;
import net.minecraft.network.play.server.S03PacketTimeUpdate;

@Module(name = "TimeChanger",cnName = "修改时间",description = "修改世界时间",category = Category.Boost)
public class TimeChanger implements Access.InstanceAccess {
    public NumberValue time = new NumberValue("时间", 14000, 0, 24000, 100);

    @EventTarget
    public void onUpdate(UpdateEvent e) {
        if (mc.theWorld != null)
            mc.theWorld.setWorldTime(time.getValue().longValue());
    }

    @EventTarget
    public void onPr(PacketReceiveEvent e){
        if(e.getPacket() instanceof S03PacketTimeUpdate) {
            e.setCancelled(true);
        }
    }

    @EventTarget
    public void onPs(PacketSendEvent e){
        if(e.getPacket() instanceof S03PacketTimeUpdate) {
            e.setCancelled(true);
        }
    }
}