package com.fpsboost.events.misc;

import com.fpsboost.events.base.Event;

public class NameEvent implements Event {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public NameEvent(String name) {
        this.name = name;
    }
}
