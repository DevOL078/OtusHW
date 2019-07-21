package ru.otus.jdbc.template;

import ru.otus.jdbc.annotation.IdAnnotationService;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Optional;
import java.util.function.Function;

public class JdbcTemplate<T> {

    private DataSource dataSource;
    private Class<T> daoClass;
    private Function<ResultSet, T> rsMapper;
    private Field identificationField;
    private QueryTemplateFabric queryTemplateFabric = new QueryTemplateFabric();

    public JdbcTemplate(Class<T> clazz, DataSource dataSource, Function<ResultSet, T> rsMapper) {
        if (clazz == null) {
            throw new IllegalArgumentException("clazz is null");
        }
        if (dataSource == null) {
            throw new IllegalArgumentException("datasource is null");
        }
        if (rsMapper == null) {
            throw new IllegalArgumentException("rsMapper is null");
        }

        this.daoClass = clazz;
        this.dataSource = dataSource;
        this.rsMapper = rsMapper;
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (IdAnnotationService.hasAnnotation(field)) {
                identificationField = field;
                return;
            }
        }
        throw new IllegalStateException(clazz.getName() + " hasn't field with @Id");
    }

    public long create(T objectDAO) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            Savepoint savepoint = connection.setSavepoint("savePointName");
            try (PreparedStatement pst = connection.prepareStatement(
                    queryTemplateFabric.createQueryTemplate(daoClass, "insert"),
                    Statement.RETURN_GENERATED_KEYS
            )) {
                Field[] fields = daoClass.getDeclaredFields();
                int index = 1;
                for (Field field : fields) {
                    if (!field.equals(identificationField)) {
                        insertValueToQuery(pst, field, objectDAO, index);
                        index++;
                    }
                }
                try {
                    pst.executeUpdate();
                    connection.commit();
                    try (ResultSet rs = pst.getGeneratedKeys()) {
                        rs.next();
                        return rs.getLong(1);
                    }
                } catch (SQLException ex) {
                    connection.rollback(savepoint);
                    System.out.println(ex.getMessage());
                }

            }
        }
        return -1;
    }

    public void update(T objectDAO) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            Savepoint savepoint = connection.setSavepoint("savePointName");
            try (PreparedStatement pst = connection.prepareStatement(
                    queryTemplateFabric.createQueryTemplate(daoClass, "update")
            )) {
                Field[] fields = daoClass.getDeclaredFields();
                int index = 1;
                for (Field field : fields) {
                    insertValueToQuery(pst, field, objectDAO, index);
                    index++;
                }
                try {
                    identificationField.setAccessible(true);
                    pst.setLong(index, (long) identificationField.get(objectDAO));
                } catch (IllegalAccessException e) {
                    throw new IllegalStateException("Can't access to field " + identificationField.getName());
                } finally {
                    identificationField.setAccessible(false);
                }
                try {
                    pst.executeUpdate();
                    connection.commit();
                } catch (SQLException ex) {
                    connection.rollback(savepoint);
                    System.out.println(ex.getMessage());
                }
            }
        }
    }

    // Return id, if objectDao updated, -1, if objectDao created
    public long createOrUpdate(T objectDAO) throws SQLException {
        long id = 0;
        try {
            identificationField.setAccessible(true);
            id = (long) identificationField.get(objectDAO);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            identificationField.setAccessible(false);
        }

        if (id != 0) {
            update(objectDAO);
            return -1;
        } else {
            return create(objectDAO);
        }
    }

    public Optional<T> load(long id) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement pst = connection.prepareStatement(
                    queryTemplateFabric.createQueryTemplate(daoClass, "select")
            )) {
                pst.setLong(1, id);
                try (ResultSet rs = pst.executeQuery()) {
                    return Optional.ofNullable(rsMapper.apply(rs));
                }
            }
        }
    }

    private void insertValueToQuery(PreparedStatement pst, Field field, Object objectDAO, int index) throws SQLException {
        try {
            field.setAccessible(true);
            pst.setObject(index, field.get(objectDAO));
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Can't access to field " + field.getName());
        } finally {
            field.setAccessible(false);
        }
    }
}
