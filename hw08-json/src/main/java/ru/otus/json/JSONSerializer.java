package ru.otus.json;

import org.apache.commons.lang3.ClassUtils;

import javax.json.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;

class JSONSerializer {

    String toJson(Object src) {
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
            String typeName = field.getType().getSimpleName();
            List<Class<?>> fieldTypeInterfaces = ClassUtils.getAllInterfaces(field.getType());
            if (field.getType().isPrimitive() || typeName.equals("String")) {
                builder.add(field.getName(), getPrimitiveJsonValue(value));
            } else if (field.getType().isArray()) {
                int arrayLength = Array.getLength(value);
                JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
                for (int i = 0; i < arrayLength; ++i) {
                    Object element = Array.get(value, i);
                    Class<?> elementClass = element.getClass();
                    if (ClassUtils.isPrimitiveOrWrapper(elementClass) || elementClass.equals(String.class)) {
                        arrayBuilder.add(getPrimitiveJsonValue(element));
                    } else {
                        arrayBuilder.add(toJsonObject(element));
                    }
                }
                builder.add(field.getName(), arrayBuilder.build());
            } else if (fieldTypeInterfaces.contains(Collection.class)) {
                Collection collection = (Collection) value;
                builder.add(field.getName(), Json.createArrayBuilder(collection).build());
            } else if (fieldTypeInterfaces.contains(Map.class)) {
                Map map = (Map) value;
                JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
                for (Object key : map.keySet()) {
                    jsonObjectBuilder.add(key.toString(), map.get(key).toString());
                }
                builder.add(field.getName(), jsonObjectBuilder.build());
            } else {
                System.err.println("Unknown type: " + typeName);
            }

            field.setAccessible(false);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            field.setAccessible(false);
        }
    }

    private JsonValue getPrimitiveJsonValue(Object value) {
        String simpleName = value.getClass().getSimpleName().toLowerCase();
        if (simpleName.contains("int")) {
            return Json.createValue((Integer) value);
        } else if (simpleName.contains("long")) {
            return Json.createValue((Long) value);
        } else if (simpleName.contains("double")) {
            return Json.createValue((Double) value);
        } else if (simpleName.contains("char")) {
            return Json.createValue((Character) value);
        } else if (simpleName.contains("string")) {
            return Json.createValue((String) value);
        } else {
            return Json.createValue(value.toString());
        }
    }

}
