package com.opom.bankingapp.repository.impl;

import com.opom.bankingapp.dto.auth.RegisterPersonalDetailsRequest;
import com.opom.bankingapp.model.UserPrincipal;
import com.opom.bankingapp.repository.UserRepository;
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
                rs.getString("role_type")
            );
        }
    }

    private Optional<UserPrincipal> findUserBy(String field, String value) {
        String sql = "SELECT u.id, u.username, u.password, r.role_type " +
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
    public long saveUser(String username, String email, String hashedPassword, long profileId, int roleId) {
        String sql = "INSERT INTO Users (username, email, password, profile_id, role_id) " +
                     "VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, hashedPassword);
            ps.setLong(4, profileId);
            ps.setInt(5, roleId);
            return ps;
        }, keyHolder);
        
        return keyHolder.getKey().longValue();
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
}
