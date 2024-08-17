package com.fpsboost.module.boost;

import com.fpsboost.Access;
import com.fpsboost.annotations.event.EventTarget;
import com.fpsboost.annotations.system.Module;
import com.fpsboost.events.misc.AttackEvent;
import com.fpsboost.module.Category;
import com.fpsboost.value.impl.ComboValue;
import com.fpsboost.value.impl.NumberValue;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.EnumParticleTypes;

@Module(name = "MoreParticles",description = "更多粒子",category = Category.Boost)
public class MoreParticles implements Access.InstanceAccess {
    private final NumberValue crackSize = new NumberValue("粒子数量", 2, 0, 10, 1);
    private final ComboValue particleMode = new ComboValue("粒子模式","暴击粒子","暴击粒子","普通攻击粒子");

    @EventTarget
    public void onA(AttackEvent e) {
        if (e.getTarget().isDead || !(e.getTarget() instanceof EntityLiving) || ((EntityLiving) e.getTarget()).getHealth() == 0) return;
        for (int index = 0; index < this.crackSize.getValue().intValue(); ++index) {
            switch (particleMode.getValue()) {
                case "暴击粒子": mc.effectRenderer.emitParticleAtEntity(e.getTarget(), EnumParticleTypes.CRIT);
                case "普通攻击粒子": mc.effectRenderer.emitParticleAtEntity(e.getTarget(), EnumParticleTypes.CRIT_MAGIC);
            }
        }
    }
}
