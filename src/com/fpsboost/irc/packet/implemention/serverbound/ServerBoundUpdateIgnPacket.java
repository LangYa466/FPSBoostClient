package com.fpsboost.irc.packet.implemention.serverbound;

import com.fpsboost.irc.packet.IRCPacket;
import com.fpsboost.irc.packet.annotations.ProtocolField;

public class ServerBoundUpdateIgnPacket implements IRCPacket {
    @ProtocolField("n")
    private final String name;

    public ServerBoundUpdateIgnPacket(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
