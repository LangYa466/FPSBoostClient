package com.fpsboost.irc.packet.implemention.clientbound;

import com.fpsboost.irc.packet.IRCPacket;
import com.fpsboost.irc.packet.annotations.ProtocolField;

import java.util.Map;

public class ClientBoundUpdateUserListPacket implements IRCPacket {
    @ProtocolField("u")
    private final Map<String,String> userMap;

    public ClientBoundUpdateUserListPacket(Map<String, String> userMap) {
        this.userMap = userMap;
    }

    public Map<String, String> getUserMap() {
        return userMap;
    }
}
