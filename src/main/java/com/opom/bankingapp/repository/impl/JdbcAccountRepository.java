package com.opom.bankingapp.repository.impl;

import com.opom.bankingapp.repository.AccountRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
}
