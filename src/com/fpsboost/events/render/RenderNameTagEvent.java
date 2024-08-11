package com.fpsboost.events.render;

import com.fpsboost.events.base.Event;

public class RenderNameTagEvent extends Event.EventCancellable {

    private String string;

    public RenderNameTagEvent(String str) {
        this.string = str;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
}
