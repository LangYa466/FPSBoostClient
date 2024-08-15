package com.fpsboost.events.misc;

import com.fpsboost.events.base.Event;
import net.minecraft.client.multiplayer.WorldClient;

public class WorldLoadEvent implements Event {
    private final WorldClient worldClient;

    public WorldClient getWorldClient() {
        return worldClient;
    }

    public WorldLoadEvent(WorldClient worldClient) {
        this.worldClient = worldClient;
    }

}