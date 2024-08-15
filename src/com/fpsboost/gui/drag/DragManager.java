package com.fpsboost.gui.drag;

import com.fpsboost.Access;
import com.fpsboost.annotations.event.EventTarget;
import com.fpsboost.events.misc.WorldLoadEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;

public class DragManager {
    public static HashMap<String, Dragging> draggable = new HashMap<>();
    private static final File DRAG_DATA = new File(Access.DIRECTORY, "drag.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();

    public Dragging createDrag(Class<?> clazz,String name, float x, float y) {
        draggable.put(name, new Dragging(clazz,name, x, y));
        return draggable.get(name);
    }

    public HashMap<String, Dragging> getDraggable() {
        return draggable;
    }

    public void saveDragData() {
        if (!DRAG_DATA.exists()) {
            DRAG_DATA.getParentFile().mkdirs();
        }
        try {
            Files.write(DRAG_DATA.toPath(), GSON.toJson(draggable.values()).getBytes(StandardCharsets.UTF_8));
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Failed to save draggables");
        }
    }

    private boolean loaded;
    @EventTarget
    public void onWorldLoad(WorldLoadEvent event) {
        if (!loaded) {
            loadDragData();
            loaded = true;
        }
    }

    public void loadDragData() {
        if (!DRAG_DATA.exists()) {
            System.out.println("No drag data found");
            return;
        }

        Dragging[] draggings;
        try {
            draggings = GSON.fromJson(new String(Files.readAllBytes(DRAG_DATA.toPath()), StandardCharsets.UTF_8), Dragging[].class);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Failed to load draggables");
            return;
        }

        for (Dragging dragging : draggings) {
            if (!draggable.containsKey(dragging.getName())) continue;
            Dragging currentDrag = draggable.get(dragging.getName());
            currentDrag.setX(dragging.getX());
            currentDrag.setY(dragging.getY());
            draggable.put(dragging.getName(), currentDrag);
        }
    }
}