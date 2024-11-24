package com.fpsboost.irc.packet.implemention.clientbound;

import com.fpsboost.irc.packet.IRCPacket;
import com.fpsboost.irc.packet.annotations.ProtocolField;

public class ClientBoundMessagePacket implements IRCPacket {
    @ProtocolField("s")
    private final String sender;

    @ProtocolField("m")
    private final String message;

    public ClientBoundMessagePacket(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }
}
