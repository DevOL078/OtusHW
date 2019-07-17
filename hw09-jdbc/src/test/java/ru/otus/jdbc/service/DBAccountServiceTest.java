package ru.otus.jdbc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.jdbc.dao.Account;
import ru.otus.jdbc.datasource.DataSourceH2;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class DBAccountServiceTest {

    private DBService<Account> dbService;

    @BeforeEach
    void beforeEach() {
        dbService = new DBAccountService(new DataSourceH2());
        dbService.createTable();
    }

    @Test
    void testSaveAndGet() {
        Account createdAccount = new Account("Free", BigDecimal.valueOf(1000));
        dbService.save(createdAccount);
        long savedNo = createdAccount.getNo();
        assertTrue(savedNo > 0);
        Account loadedAccount = dbService.get(savedNo);
        assertEquals(createdAccount, loadedAccount);
    }

}