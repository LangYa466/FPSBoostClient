package com.fpsboost.events.misc;

import com.fpsboost.events.base.Cancellable;
import com.fpsboost.events.base.Event;

/**
 * @author LangYa
 * @since 2024/6/5 下午9:02
 */

public class TextEvent implements Cancellable, Event {
    private boolean cancel;
    public String text;

    public TextEvent(String text) {
        this.text = text;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean state) {
        this.cancel = state;
    }
}
