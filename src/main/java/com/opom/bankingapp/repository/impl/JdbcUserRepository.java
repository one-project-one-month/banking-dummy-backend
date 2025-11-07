package com.opom.bankingapp.repository.impl;

import com.opom.bankingapp.dto.admin.AdminApprovalDetails;
import com.opom.bankingapp.dto.admin.UserAdminResponse;
import com.opom.bankingapp.dto.auth.RegisterPersonalDetailsRequest;
import com.opom.bankingapp.model.UserPrincipal;
import com.opom.bankingapp.repository.UserRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class JdbcUserRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static class UserPrincipalRowMapper implements RowMapper<UserPrincipal> {
        @Override
        public UserPrincipal mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new UserPrincipal(
                rs.getLong("id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("role_type"),
                rs.getString("email")
            );
        }
    }

    private Optional<UserPrincipal> findUserBy(String field, String value) {
        String sql = "SELECT u.id, u.username, u.email, u.password, r.role_type " +
                     "FROM Users u " +
                     "JOIN Role r ON u.role_id = r.id " +
                     "WHERE u." + field + " = ?";
        try {
            UserPrincipal user = jdbcTemplate.queryForObject(sql, new UserPrincipalRowMapper(), value);
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<UserPrincipal> findByUsername(String username) {
        return findUserBy("username", username);
    }
    
    @Override
    public Optional<UserPrincipal> findByEmail(String email) {
        return findUserBy("email", email);
    }

    @Override
    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM Users WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    @Override
    public long saveProfileDetail(RegisterPersonalDetailsRequest request) {
        String sql = "INSERT INTO Profile_detail (fullname, date_of_birth, gender_id, nationality_id) " +
                     "VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, request.fullname());
            ps.setObject(2, request.dateOfBirth());
            ps.setInt(3, request.genderId());
            ps.setInt(4, request.nationalityId());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    @Override
    public long saveUser(String username, String email, String hashedPassword, long profileId, int roleId, int statusId) {
        String sql = "INSERT INTO Users (username, email, password, profile_id, role_id, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, hashedPassword);
            ps.setLong(4, profileId);
            ps.setInt(5, roleId);
            ps.setInt(6, statusId);
            return ps;
        }, keyHolder);
        
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public void saveKyc(String kycType, String kycData, long profileId) {
        String sql = "INSERT INTO KYC (kyc_type, kyc_data, profile_id) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, kycType, kycData, profileId);
    }

    @Override
    public int getRoleId(String roleType) {
        String sql = "SELECT id FROM Role WHERE role_type = ?";
         try {
             return jdbcTemplate.queryForObject(sql, Integer.class, roleType);
        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException("Role not found: " + roleType);
        }
    }

    @Override
    @Transactional
    public void updatePin(Long userId, String hashedPin) {
        String sql = "UPDATE Profile_detail pd " +
                "JOIN Users u ON u.profile_id = pd.id " +
                "SET pd.pin = ? " +
                "WHERE u.id = ?";
        jdbcTemplate.update(sql, hashedPin, userId);
    }

    @Override
    @Transactional
    public void updatePolicyAgreement(Long userId, boolean agreement) {
        String sql = "UPDATE Profile_detail pd " +
                "JOIN Users u ON u.profile_id = pd.id " +
                "SET pd.is_policy_agreement = ? " +
                "WHERE u.id = ?";
        jdbcTemplate.update(sql, agreement, userId);
    }

    @Override
    public Optional<String> findFullNameByUserId(Long userId) {
        String sql = "SELECT pd.fullname FROM Profile_detail pd " +
                "JOIN Users u ON u.profile_id = pd.id " +
                "WHERE u.id = ?";
        try {
            String name = jdbcTemplate.queryForObject(sql, String.class, userId);
            return Optional.ofNullable(name);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public void updateAutoSaveReceipt(Long userId, boolean flag) {
        String sql = "UPDATE Profile_detail pd " +
                "JOIN Users u ON u.profile_id = pd.id " +
                "SET pd.is_auto_save_receipt = ? " +
                "WHERE u.id = ?";
        jdbcTemplate.update(sql, flag, userId);
    }

    @Override
    public Optional<String> findHashedPasswordById(Long userId) {
        String sql = "SELECT password FROM Users WHERE id = ?";
        try {
            String password = jdbcTemplate.queryForObject(sql, String.class, userId);
            return Optional.ofNullable(password);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<String> findHashedPinById(Long userId) {
        String sql = "SELECT pd.pin FROM Profile_detail pd " +
                "JOIN Users u ON u.profile_id = pd.id " +
                "WHERE u.id = ?";
        try {
            String pin = jdbcTemplate.queryForObject(sql, String.class, userId);
            return Optional.ofNullable(pin);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public void updatePassword(Long userId, String newHashedPassword) {
        String sql = "UPDATE Users SET password = ? WHERE id = ?";
        jdbcTemplate.update(sql, newHashedPassword, userId);
    }

    @Override
    @Transactional
    public void updateSelectedAccount(Long userId, int accountId) {
        String sql = "UPDATE Profile_detail pd " +
                "JOIN Users u ON u.profile_id = pd.id " +
                "SET pd.selected_account_id = ? " +
                "WHERE u.id = ?";
        jdbcTemplate.update(sql, accountId, userId);
    }

    @Override
    public Optional<Long> findSelectedAccountIdByUserId(Long userId) {
        String sql = "SELECT pd.selected_account_id FROM Profile_detail pd " +
                "JOIN Users u ON u.profile_id = pd.id " +
                "WHERE u.id = ?";
        try {
            Long accountId = jdbcTemplate.queryForObject(sql, Long.class, userId);
            return Optional.ofNullable(accountId);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public void updateStatus(Long userId, int statusId) {
        String sql = "UPDATE Users SET status = ? WHERE id = ?";
        jdbcTemplate.update(sql, statusId, userId);
    }

    @Override
    public Optional<AdminApprovalDetails> findApprovalDetailsById(Long userId) {
        String sql = "SELECT username, email FROM Users WHERE id = ?";
        try {
            AdminApprovalDetails details = jdbcTemplate.queryForObject(sql,
                    (rs, rowNum) -> new AdminApprovalDetails(rs.getString("username"), rs.getString("email")),
                    userId);
            return Optional.ofNullable(details);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private static class UserAdminResponseRowMapper implements RowMapper<UserAdminResponse> {
        @Override
        public UserAdminResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
            boolean status = rs.getInt("user_status") == 2;

            return new UserAdminResponse(
                    rs.getLong("user_id"),
                    rs.getString("fullname"),
                    rs.getString("email"),
                    rs.getString("role_type"),
                    rs.getString("organization_name"),
                    status,
                    rs.getTimestamp("created_at").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                    Optional.ofNullable(rs.getTimestamp("updated_at"))
                            .map(t -> t.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                            .orElse(null)
            );
        }
    }

    private static final String FIND_ALL_USERS_SQL = """
        SELECT 
            u.id AS user_id,
            pd.fullname,
            u.email,
            r.role_type,
            o.name AS organization_name,
            u.status AS user_status,
            u.created_at,
            u.updated_at
        FROM Users u
        JOIN Profile_detail pd ON u.profile_id = pd.id
        JOIN Role r ON u.role_id = r.id
        LEFT JOIN Organization o ON pd.organization_id = o.id
    """;

    @Override
    public List<UserAdminResponse> findAllUsersForAdmin() {
        String sql = FIND_ALL_USERS_SQL + " ORDER BY u.created_at DESC";
        return jdbcTemplate.query(sql, new UserAdminResponseRowMapper());
    }
}
