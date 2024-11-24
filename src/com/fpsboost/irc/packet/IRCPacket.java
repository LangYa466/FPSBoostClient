package com.fpsboost.irc.packet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.fpsboost.irc.packet.annotations.ProtocolField;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IRCPacket {
    Gson gson = new Gson();
    default void readPacket(JsonObject jsonObject) {
        for (Field field : this.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(ProtocolField.class)) {
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                field.setAccessible(true);
                ProtocolField protocolField = field.getAnnotation(ProtocolField.class);
                try {
                    JsonElement element = jsonObject.get(protocolField.value());
                    if (element == null) continue;

                    Class<?> fieldType = field.getType();
                    Object value;

                    Type genericType = field.getGenericType();
                    if (List.class.isAssignableFrom(fieldType)) {
                        value = gson.fromJson(element, TypeToken.get(genericType).getType());
                    } else if (Set.class.isAssignableFrom(fieldType)) {
                        value = gson.fromJson(element, TypeToken.get(genericType).getType());
                    } else if (Map.class.isAssignableFrom(fieldType)) {
                        value = gson.fromJson(element, TypeToken.get(genericType).getType());
                    } else {
                        value = gson.fromJson(element, fieldType);
                    }

                    field.set(this, value);

                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Error while reading packet", e);
                }
            }
        }
    }

    default JsonObject writePacket() {
        JsonObject jsonObject = new JsonObject();
        for (Field field : this.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(ProtocolField.class)) {
                if (Modifier.isStatic(field.getModifiers())) {
                    throw new RuntimeException("Protocol field can't be static.");
                }
                field.setAccessible(true);
                ProtocolField protocolField = field.getAnnotation(ProtocolField.class);
                try {
                    Object value = field.get(this);

                    if (value instanceof List || value instanceof Set) {
                        JsonArray jsonArray = gson.toJsonTree(value).getAsJsonArray();
                        jsonObject.add(protocolField.value(), jsonArray);
                    } else if (value instanceof Map) {
                        JsonElement jsonElement = gson.toJsonTree(value);
                        jsonObject.add(protocolField.value(), jsonElement);
                    } else {
                        jsonObject.addProperty(protocolField.value(), value.toString());
                    }

                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Error while writing packet", e);
                }
            }
        }
        return jsonObject;
    }
}
