package com.fpsboost.events.misc;

import com.fpsboost.events.base.Event;

public class KeyInputEvent implements Event {

    private final int key;

    public KeyInputEvent(int key) {
        this.key = key;
    }

    public int getKey() {
        return this.key;
    }

}
