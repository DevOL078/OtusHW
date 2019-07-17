package ru.otus.jdbc.template;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.jdbc.dao.Account;
import ru.otus.jdbc.datasource.DataSourceH2;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class JdbcTemplateTestAccount {

    private static DataSource dataSource;
    private JdbcTemplate<Account> jdbcTemplate;

    @BeforeAll
    static void beforeAll() throws SQLException {
        dataSource = new DataSourceH2();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pst = connection.prepareStatement(
                     "create table if not exists account( no bigint(20) NOT NULL auto_increment, type varchar(255), rest number)")) {
            pst.executeUpdate();
        }
    }

    @BeforeEach
    void before() {
        jdbcTemplate = new JdbcTemplate<>(
                Account.class,
                dataSource,
                resultSet -> {
                    try {
                        if (resultSet.next()) {
                            Account account = new Account(
                                    resultSet.getString("type"),
                                    resultSet.getBigDecimal("rest")
                            );
                            account.setNo(resultSet.getLong("no"));
                            return account;
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    return null;
                });
    }

    @Test
    void testSave() throws SQLException {
        Account createdAccount = new Account("Easy", BigDecimal.valueOf(1000));
        long no = jdbcTemplate.create(createdAccount);
        createdAccount.setNo(no);
        Account loadedAccount = jdbcTemplate.load(no).get();
        assertEquals(createdAccount, loadedAccount);
    }

    @Test
    void testUpdate() throws SQLException {
        Account createdAccount = new Account("Easy", BigDecimal.valueOf(1000));
        long no = jdbcTemplate.create(createdAccount);
        createdAccount.setNo(no);
        createdAccount.setType("Hard");
        jdbcTemplate.update(createdAccount);
        Account loadedAccount = jdbcTemplate.load(createdAccount.getNo()).get();
        assertEquals(createdAccount, loadedAccount);
    }

    @Test
    void testCreateOrUpdate() throws SQLException {
        //Create and save user
        Account createdAccount = new Account("Free", BigDecimal.valueOf(1000));
        long no = jdbcTemplate.createOrUpdate(createdAccount);
        assertNotEquals(-1, no);
        createdAccount.setNo(no);

        //Load user for checking
        Account loadedAccount = jdbcTemplate.load(no).get();
        assertEquals(createdAccount, loadedAccount);

        //Change user and save
        loadedAccount.setRest(BigDecimal.valueOf(500));
        long newNo = jdbcTemplate.createOrUpdate(loadedAccount);
        assertEquals(-1, newNo);

        //Load updated user
        Account updatedAccount = jdbcTemplate.load(loadedAccount.getNo()).get();
        assertEquals(loadedAccount, updatedAccount);
    }

}