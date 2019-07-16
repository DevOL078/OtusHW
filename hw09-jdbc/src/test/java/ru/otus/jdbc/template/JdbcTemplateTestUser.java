package ru.otus.jdbc.template;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.jdbc.dao.User;
import ru.otus.jdbc.service.DataSourceH2;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class JdbcTemplateTestUser {

    private static DataSource dataSource;
    private JdbcTemplate<User> jdbcTemplate;

    @BeforeAll
    static void beforeAll() throws SQLException {
        dataSource = new DataSourceH2();
        try(Connection connection = dataSource.getConnection();
            PreparedStatement pst = connection.prepareStatement(
                    "create table user ( id bigint(20) NOT NULL auto_increment, name varchar(255), age int(3))")) {
            pst.executeUpdate();
        }
    }

    @BeforeEach
    void before() {
        jdbcTemplate = new JdbcTemplate<>(
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

    @Test
    void testSave() throws SQLException {
        User createdUser = new User("David", 25);
        long id = jdbcTemplate.create(createdUser);
        createdUser.setId(id);
        User loadedUser = jdbcTemplate.load(id).get();
        assertEquals(createdUser, loadedUser);
    }

    @Test
    void testUpdate() throws SQLException {
        User createdUser = new User("David", 25);
        long id = jdbcTemplate.create(createdUser);
        createdUser.setId(id);
        createdUser.setName("Frodo");
        jdbcTemplate.update(createdUser);
        User loadedUser = jdbcTemplate.load(createdUser.getId()).get();
        assertEquals(createdUser, loadedUser);
    }

}