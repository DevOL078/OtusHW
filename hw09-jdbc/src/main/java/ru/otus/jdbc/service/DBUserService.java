package ru.otus.jdbc.service;

import ru.otus.jdbc.dao.User;
import ru.otus.jdbc.template.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class DBUserService implements DBService<User> {

    private DataSource dataSource;
    private JdbcTemplate<User> jdbcTemplate;

    public DBUserService(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate<>(
                User.class,
                dataSource,
                resultSet -> {
                    try {
                        if (resultSet.next()) {
                            User user = new User(
                                    resultSet.getString("name"),
                                    resultSet.getInt("age")
                            );
                            user.setId(resultSet.getLong("id"));
                            return user;
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    return null;
                });
    }

    @Override
    public void createTable() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pst = connection.prepareStatement(
                     "create table if not exists user( id bigint(20) NOT NULL auto_increment, name varchar(255), age int(3))")) {
            pst.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(User objectDao) {
        try {
            long id = jdbcTemplate.createOrUpdate(objectDao);
            if (id != -1) {
                objectDao.setId(id);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public User get(long id) {
        try {
            Optional<User> optUser = jdbcTemplate.load(id);
            return optUser.orElse(null);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
