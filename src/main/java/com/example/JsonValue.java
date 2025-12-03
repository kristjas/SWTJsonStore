package com.example;

import java.util.*;

public class JsonValue {
    private final Object value;

    public JsonValue(Object value) { this.value = value; }

    public boolean isNull() { return value == null; }
    public boolean isObject() { return value instanceof Map; }
    public boolean isArray() { return value instanceof List; }
    public boolean isString() { return value instanceof String; }
    public boolean isNumber() { return value instanceof Number; }
    public boolean isBoolean() { return value instanceof Boolean; }

    @SuppressWarnings("unchecked")
    public Map<String, JsonValue> asObject() { return (Map<String, JsonValue>) value; }

    @SuppressWarnings("unchecked")
    public List<JsonValue> asArray() { return (List<JsonValue>) value; }

    public String asString() { return (String) value; }
    public Number asNumber() { return (Number) value; }
    public Boolean asBoolean() { return (Boolean) value; }

    public JsonValue get(String key) {
        if (!isObject()) return new JsonValue(null);
        return asObject().getOrDefault(key, new JsonValue(null));
    }

    public JsonValue get(int index) {
        if (!isArray()) return new JsonValue(null);
        List<JsonValue> arr = asArray();
        if (index < 0 || index >= arr.size()) return new JsonValue(null);
        return arr.get(index);
    }

    @Override
    public String toString() {
        if (isNull()) return "null";
        if (isString()) return "\"" + asString() + "\"";
        if (isNumber() || isBoolean()) return String.valueOf(value);
        if (isArray()) return asArray().toString();
        if (isObject()) return asObject().toString();
        return String.valueOf(value);
    }
}
