package com.opom.bankingapp.repository.impl;

import com.opom.bankingapp.dto.user.AccountDetailResponse;
import com.opom.bankingapp.repository.AccountRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class JdbcAccountRepository implements AccountRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Double> findBalanceByUserId(long userId) {
        String sql = "SELECT current_balance FROM Account_detail WHERE user_id = ? LIMIT 1";
        try {
            Double balance = jdbcTemplate.queryForObject(sql, Double.class, userId);
            return Optional.ofNullable(balance);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private static class AccountDetailRowMapper implements RowMapper<AccountDetailResponse> {
        @Override
        public AccountDetailResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new AccountDetailResponse(
                    rs.getInt("id"),
                    rs.getString("account_number"),
                    rs.getDouble("current_balance")
            );
        }
    }

    @Override
    public List<AccountDetailResponse> findAccountsByUserId(Long userId) {
        String sql = "SELECT id, account_number, current_balance FROM Account_detail WHERE user_id = ?";
        return jdbcTemplate.query(sql, new AccountDetailRowMapper(), userId);
    }

    @Override
    public Optional<Double> findBalanceByAccountId(Long accountId) {
        String sql = "SELECT current_balance FROM Account_detail WHERE id = ?";
        try {
            Double balance = jdbcTemplate.queryForObject(sql, Double.class, accountId);
            return Optional.ofNullable(balance);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void updateBalance(Long accountId, double newBalance) {
        String sql = "UPDATE Account_detail SET current_balance = ? WHERE id = ?";
        jdbcTemplate.update(sql, newBalance, accountId);
    }

    @Override
    public Optional<AccountDetailResponse> findAccountDetailsById(Long accountId) {
        String sql = "SELECT id, account_number, current_balance FROM Account_detail WHERE id = ?";
        try {
            AccountDetailResponse account = jdbcTemplate.queryForObject(sql, new AccountDetailRowMapper(), accountId);
            return Optional.ofNullable(account);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public long createAccount(long userId, String accountNumber, int accountTypeId, double initialBalance, long createdBy) {
        String sql = "INSERT INTO Account_detail (user_id, account_number, account_type_id, current_balance, created_by, created_at, updated_by, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, NOW(), ?, NOW())";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, userId);
            ps.setString(2, accountNumber);
            ps.setInt(3, accountTypeId);
            ps.setDouble(4, initialBalance);
            ps.setLong(5, createdBy);
            ps.setLong(6, createdBy);
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }
}
