package com.fpsboost.events.network;

import net.minecraft.network.Packet;
import com.fpsboost.events.base.Event;

public class PacketReceiveEvent extends Event.EventCancellable {

    private final Packet<?> packet;

    public PacketReceiveEvent(Packet<?> packet) {
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return packet;
    }
}
