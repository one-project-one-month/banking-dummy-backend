package com.opom.bankingapp.features.account.repository.impl;

import com.opom.bankingapp.dto.account.AccountAdminResponse;
import com.opom.bankingapp.dto.common.OptionDto;
import com.opom.bankingapp.features.account.repository.AccountAdminRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcAccountAdminRepository implements AccountAdminRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountAdminRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static class AccountAdminResponseRowMapper implements RowMapper<AccountAdminResponse> {
        @Override
        public AccountAdminResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
            OptionDto accountType = new OptionDto(
                rs.getInt("account_type_id"),
                rs.getString("account_type_name")
            );

            boolean status = rs.getInt("user_status") == 2; 

            return new AccountAdminResponse(
                    rs.getLong("id"),
                    rs.getString("account_number"),
                    rs.getString("account_holder_fullname"),
                    accountType,
                    status,
                    rs.getTimestamp("created_at").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                    Optional.ofNullable(rs.getTimestamp("updated_at"))
                            .map(t -> t.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                            .orElse(null),
                    rs.getLong("created_by"),
                    rs.getLong("updated_by")
            );
        }
    }

    private static final AccountAdminResponseRowMapper ACCOUNT_ADMIN_MAPPER = new AccountAdminResponseRowMapper();

    private static final String FIND_ALL_ACCOUNTS_SQL = """
        SELECT 
            ad.id, 
            ad.account_number, 
            pd.fullname AS account_holder_fullname,
            at.id AS account_type_id,
            at.code AS account_type_name,
            u.status AS user_status, 
            ad.created_at, 
            ad.updated_at, 
            ad.created_by, 
            ad.updated_by
        FROM Account_detail ad
        JOIN Users u ON ad.user_id = u.id
        JOIN Profile_detail pd ON u.profile_id = pd.id
        LEFT JOIN Account_type at ON ad.account_type_id = at.id
    """;

    @Override
    public List<AccountAdminResponse> findAllAccounts() {
        return jdbcTemplate.query(FIND_ALL_ACCOUNTS_SQL + " ORDER BY ad.created_at DESC", ACCOUNT_ADMIN_MAPPER);
    }

    @Override
    public Optional<AccountAdminResponse> findAccountById(Long accountId) {
        String sql = FIND_ALL_ACCOUNTS_SQL + " WHERE ad.id = ?";
        try {
            AccountAdminResponse account = jdbcTemplate.queryForObject(sql, ACCOUNT_ADMIN_MAPPER, accountId);
            return Optional.ofNullable(account);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
