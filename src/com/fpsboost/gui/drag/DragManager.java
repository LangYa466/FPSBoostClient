package com.fpsboost.gui.drag;

import java.util.HashMap;

public class DragManager {
    public static HashMap<String, Dragging> draggable = new HashMap<>();

    public Dragging createDrag(Class<?> clazz,String name, float x, float y) {
        draggable.put(name, new Dragging(clazz,name, x, y));
        return draggable.get(name);
    }

    public HashMap<String, Dragging> getDraggable() {
        return draggable;
    }
}