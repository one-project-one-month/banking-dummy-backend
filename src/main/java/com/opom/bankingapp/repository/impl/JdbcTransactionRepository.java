package com.opom.bankingapp.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.opom.bankingapp.dto.admin.DepositResponse;
import com.opom.bankingapp.dto.user.AccountDetailResponse;
import com.opom.bankingapp.dto.user.RecentTransfer;
import com.opom.bankingapp.dto.user.UserSummary;
import com.opom.bankingapp.model.TransactionType;
import com.opom.bankingapp.repository.TransactionRepository;

@Repository
public class JdbcTransactionRepository implements TransactionRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTransactionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static class RecentTransferRowMapper implements RowMapper<RecentTransfer> {
        @Override
        public RecentTransfer mapRow(ResultSet rs, int rowNum) throws SQLException {
            UserSummary user = new UserSummary(
                    rs.getInt("recipient_user_id"),
                    rs.getString("recipient_fullname")
            );
            AccountDetailResponse account = new AccountDetailResponse(
                    rs.getInt("recipient_account_id"),
                    rs.getString("recipient_account_number"),
                    rs.getDouble("transaction_amount")
            );
            boolean isIncome = rs.getBoolean("is_income");

            int typeCode = rs.getInt("transaction_type");
            TransactionType type = (typeCode > 0) ? TransactionType.fromCode(typeCode) : TransactionType.TRANSFER;

            return new RecentTransfer(
                    rs.getLong("id"),
                    rs.getLong("id"), // mapping id to transactionId as well
                    rs.getBigDecimal("transaction_amount"),
                    type,
                    true,
                    rs.getTimestamp("created_at"),
                    rs.getTimestamp("updated_at"),
                    user,
                    account,
                    isIncome
            );
        }
    }

    @Override
    public List<RecentTransfer> findRecentTransfersByUserId(Long userId) {
        String sql = "SELECT " +
                "    t.id, " +
                "    t.created_at, " +
                "    t.updated_at, " +
                "    t.transaction_type, " +
                "    t.amount AS transaction_amount, " +
                "    u_recipient.id AS recipient_user_id, " +
                "    pd_recipient.fullname AS recipient_fullname, " +
                "    ad_recipient.id AS recipient_account_id, " +
                "    ad_recipient.account_number AS recipient_account_number, " +
                "    FALSE AS is_income " +
                "FROM Transaction t " +
                "JOIN Account_detail ad_sender ON t.debit_account_id = ad_sender.id " +
                "JOIN Account_detail ad_recipient ON t.credit_account_id = ad_recipient.id " +
                "JOIN Users u_recipient ON ad_recipient.user_id = u_recipient.id " +
                "JOIN Profile_detail pd_recipient ON u_recipient.profile_id = pd_recipient.id " +
                "WHERE ad_sender.user_id = ? " +
                "ORDER BY t.created_at DESC " +
                "LIMIT 5";

        return jdbcTemplate.query(sql, new RecentTransferRowMapper(), userId);
    }

    @Override
    public void saveTransaction(Long fromAccountId, Long toAccountId, double amount, Long createdBy) {
        String insertTxSql = "INSERT INTO Transaction (debit_account_id, credit_account_id, amount, transaction_type, created_by, created_at) VALUES (?, ?, ?, 3, ?, NOW())";
        jdbcTemplate.update(insertTxSql, fromAccountId, toAccountId, amount, createdBy);
    }

    @Override
    public void saveTransactionWithType(Long fromAccountId, Long toAccountId, double amount, Integer transactionType, Long createdBy) {
        String insertTxSql = "INSERT INTO Transaction (debit_account_id, credit_account_id, amount, transaction_type, created_by, created_at) VALUES (?, ?, ?, ?, ?, NOW())";
        jdbcTemplate.update(insertTxSql, fromAccountId, toAccountId, amount, transactionType, createdBy);
    }

    @Override
    public List<RecentTransfer> findTransactionHistoryByUserId(Long userId) {
        String sql = """
            SELECT 
                t.id,
                t.created_at, 
                t.updated_at,
                t.transaction_type,
                t.amount AS transaction_amount, 
                u_recipient.id AS recipient_user_id, 
                pd_recipient.fullname AS recipient_fullname, 
                ad_recipient.id AS recipient_account_id, 
                ad_recipient.account_number AS recipient_account_number,
                FALSE AS is_income
            FROM Transaction t 
            JOIN Account_detail ad_sender ON t.debit_account_id = ad_sender.id 
            JOIN Account_detail ad_recipient ON t.credit_account_id = ad_recipient.id 
            JOIN Users u_recipient ON ad_recipient.user_id = u_recipient.id 
            JOIN Profile_detail pd_recipient ON u_recipient.profile_id = pd_recipient.id 
            WHERE ad_sender.user_id = ?

            UNION ALL

            SELECT 
                t.id,
                t.created_at, 
                t.updated_at,
                t.transaction_type,
                t.amount AS transaction_amount, 
                u_sender.id AS recipient_user_id, 
                pd_sender.fullname AS recipient_fullname, 
                ad_sender.id AS recipient_account_id, 
                ad_sender.account_number AS recipient_account_number,
                TRUE AS is_income
            FROM Transaction t 
            JOIN Account_detail ad_sender ON t.debit_account_id = ad_sender.id 
            JOIN Account_detail ad_recipient ON t.credit_account_id = ad_recipient.id 
            JOIN Users u_sender ON ad_sender.user_id = u_sender.id 
            JOIN Profile_detail pd_sender ON u_sender.profile_id = pd_sender.id 
            WHERE ad_recipient.user_id = ?

            ORDER BY created_at DESC
        """;

        return jdbcTemplate.query(sql, new RecentTransferRowMapper(), userId, userId);
    }
	
    @Override
    public Optional<Long> findTransactionById(Long id) {
        String sql = "SELECT id FROM `Transaction` WHERE id = ?";
        try {
            Long foundId = jdbcTemplate.queryForObject(sql, Long.class, id);
            return Optional.of(foundId);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private static final RowMapper<DepositResponse> DEPOSIT_MAPPER = (rs, rowNum) -> {
        AccountDetailResponse accountDetail = new AccountDetailResponse(
                rs.getInt("account_id"),
                rs.getString("account_number"),
                rs.getDouble("account_balance")
        );
        return new DepositResponse(
                rs.getLong("id"),
                rs.getLong("id"),
                rs.getLong("credit_account_id"),
                rs.getBigDecimal("amount"),
                TransactionType.fromCode(rs.getInt("transaction_type")),
                true,
                rs.getTimestamp("created_at"),
                rs.getTimestamp("updated_at"),
                accountDetail
        );
    };

    @Override
    public List<DepositResponse> findDepositsByUserId(Long userId) {
        String sql = """
            SELECT 
                t.id,
                t.credit_account_id,
                t.amount,
                t.transaction_type,
                t.created_at,
                t.updated_at,
                ad.id AS account_id,
                ad.account_number,
                ad.current_balance AS account_balance
            FROM Users u
            JOIN Profile_detail p ON u.profile_id = p.id
            JOIN Transaction t ON t.credit_account_id = p.selected_account_id
            JOIN Account_detail ad ON t.credit_account_id = ad.id
            WHERE u.id = ? AND t.transaction_type = 1
            ORDER BY t.created_at DESC
            """;

        return jdbcTemplate.query(sql, DEPOSIT_MAPPER, userId);
    }

    @Override
    public List<DepositResponse> findAllDeposits() {
        String sql = """
            SELECT 
                t.id,
                t.credit_account_id,
                t.amount,
                t.transaction_type,
                t.created_at,
                t.updated_at,
                ad.id AS account_id,
                ad.account_number,
                ad.current_balance AS account_balance
            FROM Transaction t
            JOIN Account_detail ad ON t.credit_account_id = ad.id
            WHERE t.transaction_type = 1
            ORDER BY t.created_at DESC
            """;

        return jdbcTemplate.query(sql, DEPOSIT_MAPPER);
    }

}