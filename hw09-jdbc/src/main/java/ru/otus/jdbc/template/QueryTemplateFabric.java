package ru.otus.jdbc.template;

import ru.otus.jdbc.annotation.IdAnnotationService;

import java.lang.reflect.Field;

public class QueryTemplateFabric {

    public String createQueryTemplate(Class daoClass, String queryType) {
        String tableName = daoClass.getSimpleName().toLowerCase();
        Field[] fields = daoClass.getDeclaredFields();
        switch (queryType) {
            case "insert": {
                return createInsertTemplate(tableName, fields);
            }
            case "select": {
                return createSelectTemplate(tableName, fields);
            }
            case "update": {
                return createUpdateTemplate(tableName, fields);
            }
        }
        return null;
    }

    private String createInsertTemplate(String tableName, Field[] fields) {
        StringBuilder builder = new StringBuilder();
        builder.append("insert into ").append(tableName)
                .append(" (");
        for (int i = 0; i < fields.length; ++i) {
            Field field = fields[i];
            if (!IdAnnotationService.hasAnnotation(field)) {
                builder.append(fields[i].getName());
                if (i < fields.length - 1) {
                    builder.append(", ");
                }
            }
        }
        builder.append(") values (");
        for (int i = 0; i < fields.length - 1; ++i) {
            builder.append("?");
            if (i < fields.length - 2) {
                builder.append(", ");
            }
        }
        builder.append(")");
        return builder.toString();
    }

    private String createSelectTemplate(String tableName, Field[] fields) {
        StringBuilder builder = new StringBuilder();
        builder.append("select *");
        builder.append(" from ").append(tableName);
        builder.append(" where ");
        Field targetField = null;
        for (Field field : fields) {
            if (IdAnnotationService.hasAnnotation(field)) {
                targetField = field;
                break;
            }
        }
        builder.append(targetField.getName()).append(" = ?");
        return builder.toString();
    }

    private String createUpdateTemplate(String tableName, Field[] fields) {
        StringBuilder builder = new StringBuilder();
        builder.append("update ").append(tableName).append(" set ");
        for (int i = 0; i < fields.length; ++i) {
            Field field = fields[i];
            builder.append(field.getName()).append(" = ?");
            if (i < fields.length - 1) {
                builder.append(", ");
            }
        }
        builder.append(" where ");
        for (Field field : fields) {
            if (IdAnnotationService.hasAnnotation(field)) {
                builder.append(field.getName()).append(" = ?");
            }
        }
        return builder.toString();
    }

}
