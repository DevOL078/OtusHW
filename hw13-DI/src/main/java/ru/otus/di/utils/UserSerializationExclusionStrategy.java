package ru.otus.di.utils;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class UserSerializationExclusionStrategy implements ExclusionStrategy {
    @Override
    public boolean shouldSkipField(FieldAttributes fieldAttributes) {
        return fieldAttributes.getName().equals("phones");
    }

    @Override
    public boolean shouldSkipClass(Class<?> aClass) {
        return false;
    }
}
