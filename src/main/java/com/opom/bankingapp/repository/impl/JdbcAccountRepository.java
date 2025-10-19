package com.opom.bankingapp.repository.impl;

import com.opom.bankingapp.dto.user.AccountDetailResponse;
import com.opom.bankingapp.repository.AccountRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
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
}
