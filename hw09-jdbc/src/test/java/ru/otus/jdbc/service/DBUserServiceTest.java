package ru.otus.jdbc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.jdbc.dao.User;
import ru.otus.jdbc.datasource.DataSourceH2;

import static org.junit.jupiter.api.Assertions.*;

class DBUserServiceTest {

    private DBService<User> dbService;

    @BeforeEach
    void beforeEach() {
        dbService = new DBUserService(new DataSourceH2());
        dbService.createTable();
    }

    @Test
    void testSaveAndGet() {
        User createdUser = new User("Frodo", 25);
        dbService.save(createdUser);
        long savedId = createdUser.getId();
        assertTrue(savedId > 0);
        User loadedUser = dbService.get(savedId);
        assertEquals(createdUser, loadedUser);
    }

}