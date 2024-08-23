package com.fpsboost.irc;

import com.fpsboost.Access;
import com.fpsboost.util.ClientUtil;
import com.yumegod.simpleirc.IRCClient;
import com.yumegod.simpleirc.IRCEvent;

public class IRCManager implements Access.InstanceAccess {
    private IRCClient client = null;

    public IRCClient getClient() {
        return client;
    }

    public void setClient(IRCClient client) {
        this.client = client;
    }

    @IRCEvent
    public static void onMessage(String message) {
        ClientUtil.chat("[IRC] " + message);
    }

}