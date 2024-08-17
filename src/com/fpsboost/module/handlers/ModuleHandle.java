package com.fpsboost.module.handlers;

import com.fpsboost.Access;
import com.fpsboost.annotations.system.Disable;
import com.fpsboost.annotations.system.Enable;
import com.fpsboost.events.EventManager;
import com.fpsboost.value.AbstractValue;
import org.lwjgl.input.Keyboard;
import com.fpsboost.module.Category;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
public final class ModuleHandle {

    private final String name;

    private final Category category;

    private final Object object;

    private ArrayList<AbstractValue<?>> values;

    private boolean state;

    private boolean visible;

    private String suffix;

    private int key;

    private String description;

    public ModuleHandle(String name, Category category, Object object) {
        this.state = false;
        this.name = name;
        this.category = category;
        this.object = object;
        this.values = new ArrayList<>();
        this.visible = true;
        this.suffix = "";
        this.key = Keyboard.KEY_NONE;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setValues(ArrayList<AbstractValue<?>> values) {
        this.values = values;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public ArrayList<AbstractValue<?>> getValues() {
        return values;
    }


    public void toggle() {
        boolean state = !this.state;
        this.state = state;
        if (state) {
            EventManager.register(object);
            invokeMethodsAnnotationPresent(Enable.class);
        } else {
            EventManager.unregister(object);
            invokeMethodsAnnotationPresent(Disable.class);
        }
    }

    public void setEnable(boolean state) {
        if (state == this.state) return;
        this.state = state;
        if (state) {
            EventManager.register(object);
            invokeMethodsAnnotationPresent(Enable.class);
        } else {
            EventManager.unregister(object);
            invokeMethodsAnnotationPresent(Disable.class);
        }
    }

    private void invokeMethodsAnnotationPresent(Class<? extends Annotation> anno) {
        for (Method method : this.object.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(anno)) {
                try {
                    Access.getInstance().getInvoke().invokeMethod(object,method);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Category getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public boolean isEnabled() {
        return state;
    }

    public String getDescription() {
        return description;
    }
}
