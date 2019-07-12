package ru.otus.json;

import org.apache.commons.lang3.ClassUtils;

import javax.json.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;

class JSONSerializer {

    String toJson(Object src) {
        Object jsonPrimitive = getPrimitiveJsonValue(src);
        if(jsonPrimitive != null) {
            return jsonPrimitive.toString();
        }
        return toJsonObject(src).toString();
    }

    private JsonObject toJsonObject(Object src) {
        Class<?> srcClass = src.getClass();
        Field[] srcFields = srcClass.getDeclaredFields();
        JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
        for (Field field : srcFields) {
            serializeField(field, src, jsonBuilder);
        }
        return jsonBuilder.build();
    }

    private void serializeField(Field field, Object src, JsonObjectBuilder builder) {
        try {
            field.setAccessible(true);
            Object value = field.get(src);
            if(value == null) {
                builder.add(field.getName(), JsonValue.NULL);
                return;
            }
            String typeName = field.getType().getSimpleName();
            List<Class<?>> fieldTypeInterfaces = ClassUtils.getAllInterfaces(field.getType());
            Object jsonValue = getPrimitiveJsonValue(value);
            if (field.getType().isPrimitive() || typeName.equals("String")) {
                builder.add(field.getName(), (JsonValue) jsonValue);
            } else if (field.getType().isArray()) {
                builder.add(field.getName(), (JsonArray) jsonValue);
            } else if (fieldTypeInterfaces.contains(Collection.class)) {
                builder.add(field.getName(), (JsonArray) jsonValue);
            } else if (fieldTypeInterfaces.contains(Map.class)) {
                builder.add(field.getName(), (JsonObject) jsonValue);
            } else {
                builder.add(field.getName(), toJsonObject(value));
            }

            field.setAccessible(false);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            field.setAccessible(false);
        }
    }

    private Object getPrimitiveJsonValue(Object value) {
        if(value == null) {
            return JsonValue.NULL;
        }
        Class<?> valueClass = value.getClass();
        List<Class<?>> valueClassInterfaces = ClassUtils.getAllInterfaces(valueClass);
        if (valueClass.equals(byte.class) || valueClass.equals(Byte.class)) {
            return Json.createValue((Byte) value);
        } else if (valueClass.equals(short.class) || valueClass.equals(Short.class)) {
            return Json.createValue((Short) value);
        } else if (valueClass.equals(int.class) || valueClass.equals(Integer.class)) {
            return Json.createValue((Integer) value);
        } else if (valueClass.equals(long.class) || valueClass.equals(Long.class)) {
            return Json.createValue((Long) value);
        } else if (valueClass.equals(float.class) || valueClass.equals(Float.class)) {
            return Json.createValue((Float) value);
        } else if (valueClass.equals(double.class) || valueClass.equals(Double.class)) {
            return Json.createValue((Double) value);
        } else if (valueClass.equals(char.class) || valueClass.equals(Character.class)) {
            return Json.createValue(value.toString());
        } else if (valueClass.equals(boolean.class) || valueClass.equals(Boolean.class)) {
            return (boolean)value ? JsonValue.TRUE : JsonValue.FALSE;
        } else if (valueClass.equals(String.class)) {
            return Json.createValue((String) value);
        } else if(valueClass.isArray()) {
            return serializeArray(value);
        } else if(valueClassInterfaces.contains(Collection.class)) {
            return serializeCollection(value);
        } else if(valueClassInterfaces.contains(Map.class)) {
            return serializeMap(value);
        }

        return null;
    }

    private JsonArray serializeArray(Object value) {
        int arrayLength = Array.getLength(value);
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (int i = 0; i < arrayLength; ++i) {
            Object element = Array.get(value, i);
            Class<?> elementClass = element.getClass();
            if (ClassUtils.isPrimitiveOrWrapper(elementClass) || elementClass.equals(String.class)) {
                arrayBuilder.add((JsonValue) getPrimitiveJsonValue(element));
            } else {
                arrayBuilder.add(toJsonObject(element));
            }
        }
        return arrayBuilder.build();
    }

    private JsonArray serializeCollection(Object value) {
        Collection collection = (Collection) value;
        return Json.createArrayBuilder(collection).build();
    }

    private JsonObject serializeMap(Object value) {
        Map map = (Map) value;
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        for (Object key : map.keySet()) {
            jsonObjectBuilder.add(key.toString(), map.get(key).toString());
        }
        return jsonObjectBuilder.build();
    }


}
