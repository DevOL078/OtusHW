package ru.otus.jdbc.template;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.jdbc.dao.User;
import ru.otus.jdbc.datasource.DataSourceH2;

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
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pst = connection.prepareStatement(
                     "create table if not exists user( id bigint(20) NOT NULL auto_increment, name varchar(255), age int(3))")) {
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
    void testCreateAndLoad() throws SQLException {
        User createdUser = new User("Max", 25);
        long id = jdbcTemplate.create(createdUser);
        createdUser.setId(id);
        User loadedUser = jdbcTemplate.load(id).get();
        assertEquals(createdUser, loadedUser);
    }

    @Test
    void testUpdate() throws SQLException {
        User createdUser = new User("Gandalf", 25);
        long id = jdbcTemplate.create(createdUser);
        createdUser.setId(id);
        createdUser.setName("Frodo");
        jdbcTemplate.update(createdUser);
        User loadedUser = jdbcTemplate.load(createdUser.getId()).get();
        assertEquals(createdUser, loadedUser);
    }

    @Test
    void testCreateOrUpdate() throws SQLException {
        //Create and save user
        User createdUser = new User("Legolas", 25);
        long id = jdbcTemplate.createOrUpdate(createdUser);
        assertNotEquals(-1, id);
        createdUser.setId(id);

        //Load user for checking
        User loadedUser = jdbcTemplate.load(id).get();
        assertEquals(createdUser, loadedUser);

        //Change user and save
        loadedUser.setName("Gollum");
        long newId = jdbcTemplate.createOrUpdate(loadedUser);
        assertEquals(-1, newId);

        //Load updated user
        User updatedUser = jdbcTemplate.load(loadedUser.getId()).get();
        assertEquals(loadedUser, updatedUser);
    }

}