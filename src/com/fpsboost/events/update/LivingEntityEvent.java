package com.fpsboost.events.update;

import com.fpsboost.events.base.Cancellable;
import com.fpsboost.events.base.Event;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;

public class LivingEntityEvent implements Cancellable, Event {
    private final EntityLivingBase entity;
    private final RendererLivingEntity<?> renderer;
    private final float partialTicks;
    private final double x, y, z;

    public LivingEntityEvent(EntityLivingBase entity, RendererLivingEntity<?> renderer, float partialTicks, double x, double y, double z) {
        this.entity = entity;
        this.renderer = renderer;
        this.partialTicks = partialTicks;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public EntityLivingBase getEntity() {
        return entity;
    }

    public RendererLivingEntity<?> getRenderer() {
        return renderer;
    }

    public float getPartialTicks() {
        return partialTicks;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    private boolean canceled;

    @Override
    public boolean isCancelled() {
        return canceled;
    }

    @Override
    public void setCancelled(boolean canceled) {
        this.canceled = canceled;
    }
}
