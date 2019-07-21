package ru.otus.jdbc.template;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.jdbc.dao.User;
import ru.otus.jdbc.datasource.DataSourceH2;

import javax.sql.DataSource;

import java.sql.*;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class JdbcTemplateTestUser {

    private static DataSource dataSource;
    private JdbcTemplate<User> jdbcTemplate;
    private Function<ResultSet, User> rsMapper = resultSet -> {
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
    };

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
                rsMapper);
    }

    @Test
    void testCreate() throws SQLException {
        User createdUser = new User("Max", 25);
        long id = jdbcTemplate.create(createdUser);
        createdUser.setId(id);
        User loadedUser = load(id).get();
        assertEquals(createdUser, loadedUser);
    }

    @Test
    void testLoad() throws SQLException {
        User createdUser = new User("Ivan", 27);
        long id = create(createdUser);
        createdUser.setId(id);
        User loadedUser = jdbcTemplate.load(id).get();
        assertEquals(createdUser, loadedUser);
    }

    @Test
    void testUpdate() throws SQLException {
        User createdUser = new User("Gandalf", 25);
        long id = create(createdUser);
        createdUser.setId(id);
        createdUser.setName("Frodo");
        jdbcTemplate.update(createdUser);
        User loadedUser = load(createdUser.getId()).get();
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
        User loadedUser = load(id).get();
        assertEquals(createdUser, loadedUser);

        //Change user and save
        loadedUser.setName("Gollum");
        long newId = jdbcTemplate.createOrUpdate(loadedUser);
        assertEquals(-1, newId);

        //Load updated user
        User updatedUser = load(loadedUser.getId()).get();
        assertEquals(loadedUser, updatedUser);
    }

    private Optional<User> load(long id) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement pst = connection.prepareStatement(
                    "select * from user where id = ?"
            )) {
                pst.setLong(1, id);
                try (ResultSet rs = pst.executeQuery()) {
                    return Optional.ofNullable(rsMapper.apply(rs));
                }
            }
        }
    }

    private long create(User user) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement pst = connection.prepareStatement(
                    "insert into user (name, age) values (?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            )) {
                pst.setString(1, user.getName());
                pst.setInt(2, user.getAge());
                pst.executeUpdate();
                connection.commit();
                try (ResultSet rs = pst.getGeneratedKeys()) {
                    rs.next();
                    return rs.getLong(1);
                }
            }
        }
    }

}