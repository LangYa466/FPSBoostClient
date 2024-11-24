package com.fpsboost.irc.packet.implemention.clientbound;

import com.fpsboost.irc.packet.IRCPacket;
import com.fpsboost.irc.packet.annotations.ProtocolField;

public class ClientBoundDisconnectPacket implements IRCPacket {
    @ProtocolField("r")
    private final String reason;

    public ClientBoundDisconnectPacket(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
