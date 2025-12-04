package com.opom.bankingapp.repository.impl;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.opom.bankingapp.dto.admin.DepositRequest;
import com.opom.bankingapp.dto.admin.DepositResponse;
import com.opom.bankingapp.model.TransactionType;
import com.opom.bankingapp.repository.DepositRepository;

@Repository
public class DepositRepositoryImpl implements DepositRepository {

    private final JdbcTemplate jdbcTemplate;

    public DepositRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    
    private static final String INSERT_SQL = """
        INSERT INTO Deposit (
            transaction_id,
            account_id,
            amount,
            transaction_type,
            status,
            created_at,
            updated_at,
            created_by,
            updated_by
        ) VALUES (?, ?, ?, ?, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, ?, ?)
        """;
    
    private static final RowMapper<DepositResponse> DEPOSIT_MAPPER = (rs, rowNum) ->
    new DepositResponse(
            rs.getLong("id"),
            rs.getLong("transaction_id"),
            rs.getLong("account_id"),
            rs.getBigDecimal("amount"),
            TransactionType.fromCode(rs.getInt("transaction_type")),
            rs.getBoolean("status"),
            rs.getTimestamp("created_at"),
            rs.getTimestamp("updated_at")
    );
    
    @Override
    public Long createDeposit(Long createdBy, DepositRequest request) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, request.transactionId());
            ps.setLong(2, request.accountId());
            ps.setBigDecimal(3, BigDecimal.valueOf(request.amount()));
            ps.setInt(4, request.transactionType().getCode());
            ps.setLong(5, createdBy);
            ps.setLong(6, createdBy);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }
    
    public List<DepositResponse> findDepositsByUserId(Long userId) {
        String sql = """
            SELECT 
                d.id,
                d.transaction_id,
                d.account_id,
                d.amount,
                d.transaction_type,
                d.status,
                d.created_at,
                d.updated_at
            FROM Users u
            JOIN Profile_detail p ON u.profile_id = p.id
            JOIN Deposit d ON d.account_id = p.selected_account_id
            WHERE u.id = ?
            ORDER BY d.created_at DESC
            """;

        return jdbcTemplate.query(sql, DEPOSIT_MAPPER, userId);
    }
}

