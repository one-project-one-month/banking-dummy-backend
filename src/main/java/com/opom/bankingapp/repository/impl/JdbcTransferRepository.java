package com.opom.bankingapp.repository.impl;

import com.opom.bankingapp.dto.transfer.TransferPrepareResponse;
import com.opom.bankingapp.repository.TransferRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class JdbcTransferRepository implements TransferRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static class TransferPrepareResponseRowMapper implements RowMapper<TransferPrepareResponse> {
        @Override
        public TransferPrepareResponse mapRow(ResultSet rs, int rowNum) throws SQLException {

            TransferPrepareResponse.ToAccountDetails toAccountDetails = new TransferPrepareResponse.ToAccountDetails(
                    rs.getInt("to_account_id"),
                    rs.getString("to_account_number")
            );

            TransferPrepareResponse.UserDetails userDetails = new TransferPrepareResponse.UserDetails(
                    rs.getLong("user_id"),
                    rs.getString("user_fullname")
            );

            return new TransferPrepareResponse(toAccountDetails, userDetails);
        }
    }

    @Override
    public Optional<TransferPrepareResponse> findNicknamePrepareDetails(Long nicknameId, Long fromUserId) {
        String sql = """
            SELECT 
                ad.id AS to_account_id, 
                ad.account_number AS to_account_number,
                u.id AS user_id,
                pd.fullname AS user_fullname
            FROM Nickname n
            JOIN Account_detail ad ON n.to_account = ad.id
            JOIN Users u ON ad.user_id = u.id
            JOIN Profile_detail pd ON u.profile_id = pd.id
            WHERE n.id = ? AND n.from_account = ?
        """;

        try {
            TransferPrepareResponse response = jdbcTemplate.queryForObject(
                    sql,
                    new TransferPrepareResponseRowMapper(),
                    nicknameId,
                    fromUserId
            );
            return Optional.ofNullable(response);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<TransferPrepareResponse> findAccountPrepareDetails(String accountNumber) {
        String sql = """
            SELECT 
                ad.id AS to_account_id, 
                ad.account_number AS to_account_number,
                u.id AS user_id,
                pd.fullname AS user_fullname
            FROM Account_detail ad
            JOIN Users u ON ad.user_id = u.id
            JOIN Profile_detail pd ON u.profile_id = pd.id
            WHERE ad.account_number = ?
        """;

        try {
            TransferPrepareResponse response = jdbcTemplate.queryForObject(
                    sql,
                    new TransferPrepareResponseRowMapper(),
                    accountNumber
            );
            return Optional.ofNullable(response);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
