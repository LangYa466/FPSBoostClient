package com.fpsboost.events.network;

import net.minecraft.network.Packet;
import com.fpsboost.events.base.Event;

public class PacketSendEvent extends Event.EventCancellable {

    private final Packet<?> packet;

    public PacketSendEvent(Packet<?> packet) {
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return packet;
    }
}
