package ru.otus.jdbc.service;

import ru.otus.jdbc.dao.Account;
import ru.otus.jdbc.template.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class DBAccountService implements DBService<Account> {

    private DataSource dataSource;
    private JdbcTemplate<Account> jdbcTemplate;

    public DBAccountService(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate<>(
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

    @Override
    public void createTable() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pst = connection.prepareStatement(
                     "create table if not exists account ( no bigint(20) NOT NULL auto_increment, type varchar(255), rest number)")) {
            pst.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Account objectDao) {
        try {
            long no = jdbcTemplate.createOrUpdate(objectDao);
            if (no != -1) {
                objectDao.setNo(no);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Account get(long no) {
        try {
            Optional<Account> optAccount = jdbcTemplate.load(no);
            return optAccount.orElse(null);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
