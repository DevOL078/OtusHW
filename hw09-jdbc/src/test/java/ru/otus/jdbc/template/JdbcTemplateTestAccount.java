package ru.otus.jdbc.template;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.jdbc.dao.Account;
import ru.otus.jdbc.datasource.DataSourceH2;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class JdbcTemplateTestAccount {

    private static DataSource dataSource;
    private JdbcTemplate<Account> jdbcTemplate;
    private Function<ResultSet, Account> rsMapper = resultSet -> {
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
    };

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
                rsMapper);
    }

    @Test
    void testCreate() throws SQLException {
        Account createdAccount = new Account("Easy", BigDecimal.valueOf(1000));
        long no = jdbcTemplate.create(createdAccount);
        createdAccount.setNo(no);
        Account loadedAccount = load(no).get();
        assertEquals(createdAccount, loadedAccount);
    }

    @Test
    void testLoad() throws SQLException {
        Account createdAccount = new Account("Easy", BigDecimal.valueOf(1000));
        long no = create(createdAccount);
        createdAccount.setNo(no);
        Account loadedAccount = jdbcTemplate.load(no).get();
        assertEquals(createdAccount, loadedAccount);
    }

    @Test
    void testUpdate() throws SQLException {
        Account createdAccount = new Account("Easy", BigDecimal.valueOf(1000));
        long no = create(createdAccount);
        createdAccount.setNo(no);
        createdAccount.setType("Hard");
        jdbcTemplate.update(createdAccount);
        Account loadedAccount = load(createdAccount.getNo()).get();
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
        Account loadedAccount = load(no).get();
        assertEquals(createdAccount, loadedAccount);

        //Change user and save
        loadedAccount.setRest(BigDecimal.valueOf(500));
        long newNo = jdbcTemplate.createOrUpdate(loadedAccount);
        assertEquals(-1, newNo);

        //Load updated user
        Account updatedAccount = load(loadedAccount.getNo()).get();
        assertEquals(loadedAccount, updatedAccount);
    }

    private Optional<Account> load(long no) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement pst = connection.prepareStatement(
                    "select * from account where no = ?"
            )) {
                pst.setLong(1, no);
                try (ResultSet rs = pst.executeQuery()) {
                    return Optional.ofNullable(rsMapper.apply(rs));
                }
            }
        }
    }

    private long create(Account account) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement pst = connection.prepareStatement(
                    "insert into account (type, rest) values (?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            )) {
                pst.setString(1, account.getType());
                pst.setBigDecimal(2, account.getRest());
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