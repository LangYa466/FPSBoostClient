package com.fpsboost.irc.packet.implemention.serverbound;

import com.fpsboost.irc.packet.IRCPacket;
import com.fpsboost.irc.packet.annotations.ProtocolField;


public class ServerBoundHandshakePacket implements IRCPacket {
    @ProtocolField("u")
    private final String username;

    @ProtocolField("t")
    private final String token;

    public ServerBoundHandshakePacket(String username, String token) {
        this.username = username;
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }
}
