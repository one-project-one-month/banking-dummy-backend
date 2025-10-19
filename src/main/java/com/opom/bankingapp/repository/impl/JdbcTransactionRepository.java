package com.opom.bankingapp.repository.impl;

import com.opom.bankingapp.dto.user.AccountDetailResponse;
import com.opom.bankingapp.dto.user.RecentTransfer;
import com.opom.bankingapp.dto.user.UserSummary;
import com.opom.bankingapp.repository.TransactionRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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
                0
            );
            return new RecentTransfer(user, account);
        }
    }

    @Override
    public List<RecentTransfer> findRecentTransfersByUserId(Long userId) {
        String sql = "SELECT " +
            "    t.created_at, " +
            "    u_recipient.id AS recipient_user_id, " +
            "    pd_recipient.fullname AS recipient_fullname, " +
            "    ad_recipient.id AS recipient_account_id, " +
            "    ad_recipient.account_number AS recipient_account_number " +
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
}