package ru.otus.jdbc.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class IdAnnotationService {

    public static boolean hasAnnotation(Field field) {
        Annotation[] declaredAnnotations = field.getDeclaredAnnotations();
        for (Annotation annotation : declaredAnnotations) {
            if (annotation.annotationType().equals(Id.class)) {
                return true;
            }
        }
        return false;
    }

}
