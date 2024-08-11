package com.fpsboost.module;

/**
 * Module Categories
 * Functions for classifying modules
 *
 * @author cubk
 */
public enum Category {
    FakeHack("伪装外挂"),ClientSetting("客户端设置"),Boost("优化"), GUI("视觉");

    private final String name;

    Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
